//flashcard.js
import React, { useState, useEffect } from 'react';
import '../styles/vocabulary.css';
import { useParams, useNavigate } from 'react-router-dom';

const Flashcard = ({ vocabularies, onUpdateVocabulary }) => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [currentQuestion, setCurrentQuestion] = useState(0);
    const [isMeaningVisible, setIsMeaningVisible] = useState(false);
    const [flashcards, setFlashcards] = useState([]);
    const [totalQuestions, setTotalQuestions] = useState(0);
    const [progress, setProgress] = useState('');
    const [isLastCard, setIsLastCard] = useState(false);
    const currentVocabId = Number(id);

    // vocabularies가 undefined인 경우를 대비한 안전장치 추가
    const availableVocabularies = vocabularies?.filter(vocab => 
        vocab.id !== currentVocabId
    ) || [];

    // 현재 단어장에 단어가 있는지 확인
    const hasWords = flashcards.length > 0;

    useEffect(() => {
        const currentVocab = vocabularies?.find(vocab => vocab.id === Number(id));
        if (currentVocab?.words) {
            const cards = currentVocab.words.map(word => ({
                word: word.word,
                meanings: word.meanings ? word.meanings.split(', ').map(meaning => {
                    if (meaning.includes('(')) {
                        const [text, partOfSpeech] = meaning.split(' (');
                        return {
                            text: text,
                            partOfSpeech: partOfSpeech.replace(')', '')
                        };
                    }
                    return {
                        text: meaning,
                        partOfSpeech: ''
                    };
                }) : []
            }));
            setFlashcards(cards);
            setTotalQuestions(cards.length);
        }
    }, [id, vocabularies]);

    useEffect(() => {
        if (totalQuestions > 0) {
            setProgress(`진행률: ${currentQuestion + 1}/${totalQuestions}`);
        }
    }, [currentQuestion, totalQuestions]);

    useEffect(() => {
        setIsLastCard(currentQuestion === totalQuestions - 1);
    }, [currentQuestion, totalQuestions]);

    const toggleFlashcard = () => {
        setIsMeaningVisible(!isMeaningVisible);
    };

    const handleNextCard = () => {
        if (isLastCard) {
            navigate(-1);
        } else {
            setCurrentQuestion(Math.min(currentQuestion + 1, flashcards.length - 1));
            setIsMeaningVisible(false);
        }
    };

    const handlePrevCard = () => {
        setCurrentQuestion(Math.max(currentQuestion - 1, 0));
        setIsMeaningVisible(false);
    };

    const playAudio = (event) => {
        event.stopPropagation();
    };

    const addVocab = (event) => {
        const selectedVocabId = Number(event.target.value);
        if (!selectedVocabId) return;

        const selectedVocab = vocabularies.find(vocab => vocab.id === selectedVocabId);
        const currentCard = flashcards[currentQuestion];

        // 선택된 단어장에 현재 단어가 이미 있는지 확인
        const isWordExists = selectedVocab.words?.some(
            word => word.word.toLowerCase() === currentCard.word.toLowerCase()
        );

        if (isWordExists) {
            alert('이미 해당 단어장에 존재하는 단어입니다.');
            event.target.value = '';
            return;
        }

        // 새 단어 객체 생성 - vocabularies의 words 배열에 들어갈 형식과 일치하도록 수정
        const newWord = {
            id: Date.now(),
            word: currentCard.word,
            meanings: currentCard.meanings.map(m => `${m.text} (${m.partOfSpeech})`).join(', ')
        };

        // 단어장 업데이트
        const updatedVocabularies = vocabularies.map(vocab => 
            vocab.id === selectedVocabId
                ? {
                    ...vocab,
                    words: [...(vocab.words || []), newWord],
                    wordCount: (vocab.words?.length || 0) + 1
                  }
                : vocab
        );

        // App 컴포넌트의 상태 업데이트
        onUpdateVocabulary(selectedVocabId, updatedVocabularies.find(v => v.id === selectedVocabId).words);
        
        alert('단어가 성공적으로 추가되었습니다.');
        event.target.value = '';
    };

    const handleBackButton = () => {
        window.history.back();
    };

    const currentCard = flashcards[currentQuestion] || {};

    return (
        <div style={{
            fontFamily: 'TTHakgyoansimEunhasuR',
            backgroundColor: '#f8f9fa',
            display: 'flex',
            flexDirection: 'column',
            height: '100vh',
            position: 'relative',
            padding: '20px'
        }}>
            <header style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                marginBottom: '20px',
                borderBottom: '1px solid #ddd',
                padding: '10px 0'
            }}>
                <button 
                    className="back-button"
                    onClick={() => navigate(-1)}
                >
                    ←
                </button>
                <p style={{
                    fontSize: '18px',
                    color: '#333'
                }}>{progress}</p>
                <div style={{ width: '40px' }}></div>
            </header>

            <main style={{
                flex: 1,
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center'
            }}>
                <div style={{
                    width: '500px',
                    height: '300px',
                    backgroundColor: '#fff',
                    borderRadius: '8px',
                    boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    fontSize: '24px',
                    textAlign: 'center',
                    position: 'relative',
                    cursor: 'pointer'
                }} onClick={toggleFlashcard}>
                    <div style={{
                        display: isMeaningVisible ? 'none' : 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        gap: '20px'
                    }}>
                        <p>{currentCard.word}</p>
                        <button 
                            className="custom-button"
                            onClick={playAudio}
                            style={{
                                position: 'absolute',
                                top: '10px',
                                left: '10px'
                            }}
                        >
                            듣기
                        </button>
                    </div>
                    <div style={{
                        display: isMeaningVisible ? 'block' : 'none'
                    }}>
                        {currentCard.meanings && currentCard.meanings.map((meaning, index) => (
                            <p key={index}>
                                {meaning.text} ({meaning.partOfSpeech})
                            </p>
                        ))}
                    </div>
                </div>
            </main>

            <footer style={{
                display: 'flex',
                justifyContent: 'space-around',
                alignItems: 'center',
                padding: '20px 0',
                borderTop: '1px solid #ddd',
                gap: '10px'
            }}>
                <button 
                    className="custom-button"
                    onClick={handlePrevCard}
                >
                    이전
                </button>
                <select 
                    onChange={addVocab} 
                    disabled={!hasWords}
                    style={{
                        fontFamily: 'TTHakgyoansimEunhasuR',
                        padding: '8px 12px',
                        border: '1px solid #ddd',
                        borderRadius: '4px',
                        fontSize: '14px',
                        backgroundColor: '#fff',
                        cursor: !hasWords ? 'not-allowed' : 'pointer',
                        opacity: !hasWords ? 0.6 : 1
                    }}
                >
                    <option value="">
                        {!hasWords 
                            ? "단어가 없습니다" 
                            : "단어장에 추가"}
                    </option>
                    {hasWords && availableVocabularies.map(vocab => (
                        <option key={vocab.id} value={vocab.id}>
                            {vocab.title}
                        </option>
                    ))}
                </select>
                <button 
                    className="custom-button"
                    onClick={handleNextCard}
                    style={{
                        ...styles.button,
                        backgroundColor: isLastCard ? '#4CAF50' : undefined
                    }}
                >
                    {isLastCard ? '완료' : '다음'}
                </button>
            </footer>
        </div>
    );
};

const styles = {
    button: {
        transition: 'background-color 0.3s ease'
    }
};

export default Flashcard;