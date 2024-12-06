//flashcard.js
import React, { useState, useEffect } from 'react';
import '../styles/vocabulary.css';
import { useParams, useNavigate } from 'react-router-dom';

const Flashcard = ({ isDarkMode, vocabId, onComplete }) => {
    const [currentQuestion, setCurrentQuestion] = useState(0);
    const [isMeaningVisible, setIsMeaningVisible] = useState(false);
    const [flashcards, setFlashcards] = useState([]);
    const [totalQuestions, setTotalQuestions] = useState(0);
    const [progress, setProgress] = useState('');
    const [isLastCard, setIsLastCard] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [result, setResult] = useState('');

    const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

    const fetchVocabData = async () => {
        try {
            const wordResponse = await fetch(`/api/words/all?vocab_id=${vocabId}`);
            const wordData = await wordResponse.json();
            if (wordData.status === 200) {
                if (wordData.data.length === 0) {
                    setFlashcards([]);
                    setResult("단어가 없습니다.");
                    setIsLoading(false);
                    return;
                }
                const wordsWithDefs = await Promise.all(wordData.data.map(async (word) => {
                    const defsData = await fetch(`/api/defs/all?word_id=${word.wordId}`)
                        .then((res) => res.json())
                        .then((data) => (data.status === 200 ? data.data : []))
                        .catch((err) => {
                            console.error("뜻 정보 불러오기 실패", err);
                            return [];
                        });

                    const statsData = await fetch(`/api/stats/detail?word_id=${word.wordId}`)
                        .then((res) => res.json())
                        .then((data) => (data.status === 200 ? data.data : []))
                        .catch((err) => {
                            console.error("통계 정보 불러오기 실패", err);
                            return [];
                        });

                    return {...word, defs: defsData, stats: statsData};
                }));

                setFlashcards(wordsWithDefs);
                setTotalQuestions(wordsWithDefs.length);
            } else {
                console.error("단어 정보 불러오기 실패");
            }
            setIsLoading(false);
        } catch (error) {
            console.error('Error fetching vocab data:', error);
            setIsLoading(false);
        }
    };

    useEffect(() => {
        console.log("flashcards", flashcards);
    }, [flashcards]);

    useEffect(() => {
        setIsLoading(true);
        setCurrentQuestion(0);
        setIsMeaningVisible(false);
        setFlashcards([]);
        setProgress('');
        setIsLastCard(false);
        setIsLoading(true);
        fetchVocabData();
    }, [vocabId]);

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
            onComplete();
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
        
        if (!flashcards[currentQuestion]) return;

        try {
            window.speechSynthesis.cancel();
            const utterance = new SpeechSynthesisUtterance(flashcards[currentQuestion].expression);
            utterance.lang = 'en-US';
            utterance.rate = 0.8;
            window.speechSynthesis.speak(utterance);
        } catch (error) {
            console.error('음성 재생 실패:', error);
        }
    };

    const handleBackButton = () => {
        window.history.back();
    };

    const currentCard = flashcards[currentQuestion] || {};

    // 페이지 이탈 시 음성 재생 중지
    useEffect(() => {
        return () => {
            window.speechSynthesis.cancel();
        };
    }, []);

    return (
        <div style={{
            fontFamily: 'TTHakgyoansimEunhasuR',
            backgroundColor: isDarkMode ? '#242526' : '#f8f9fa',
            display: 'flex',
            flexDirection: 'column',
            height: '100vh',
            position: 'relative',
            padding: '20px',
            color: isDarkMode ? '#e4e6eb' : '#000',
            borderRadius: '20px',
            boxShadow: isDarkMode ? '0 0 20px rgba(0,0,0,0.3)' : '0 0 20px rgba(0,0,0,0.1)'
        }}>
            <header style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                borderBottom: `2px solid ${isDarkMode ? '#404040' : '#ddd'}`,
                padding: '10px',
                height: '10%',
                fontFamily: 'TTHakgyoansimEunhasuR'
            }}>
                <p>{currentQuestion + 1 + "/" + totalQuestions}</p>
            </header>

            <main style={{
                flex: 1,
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                padding: '20px'
            }}>
                {flashcards.length > 0 ? (
                    <div
                        onClick={toggleFlashcard}
                        style={{
                            width: '500px',
                            height: '300px',
                            backgroundColor: isDarkMode ? '#3a3b3c' : 'white',
                            borderRadius: '8px',
                            boxShadow: '0 4px 8px rgba(0,0,0,0.1)',
                            display: 'flex',
                            flexDirection: 'column',
                            justifyContent: 'center',
                            alignItems: 'center',
                            cursor: 'pointer',
                            padding: '20px',
                            textAlign: 'center',
                            position: 'relative'
                        }}
                    >
                        <button
                            onClick={(e) => {
                                e.stopPropagation();
                                playAudio(e);
                            }}
                            style={{
                                position: 'absolute',
                                top: '20px',
                                left: '20px',
                                padding: '8px',
                                backgroundColor: 'transparent',
                                border: 'none',
                                cursor: 'pointer',
                                fontSize: '24px'
                            }}
                        >
                            🔊
                        </button>
                        {!isMeaningVisible ? (
                            <h2 style={{ fontSize: '35px' }}>
                                {flashcards[currentQuestion].expression}
                            </h2>
                        ) : (
                            <div>
                                {flashcards[currentQuestion].defs.map((def, index) => (
                                    <p key={index} style={{ 
                                        fontSize: '25px',
                                        margin: '10px 0'
                                    }}>
                                        {def.definition} ({def.type})
                                    </p>
                                ))}
                            </div>
                        )}
                    </div>
                ) : (
                    <p>단어가 없습니다.</p>
                )}
            </main>

            <footer style={{
                display: 'flex',
                justifyContent: 'space-around',
                alignItems: 'center',
                padding: '20px 0',
                borderTop: `1px solid ${isDarkMode ? '#4a4b4c' : '#ddd'}`,
                gap: '10px'
            }}>
                <button 
                    className="flashcard-button"
                    onClick={handlePrevCard}
                >
                    이전
                </button>
                <button 
                    className="flashcard-button"
                    onClick={handleNextCard}
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