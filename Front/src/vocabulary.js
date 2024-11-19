import React, { useState } from 'react';
import './Vocabulary.css';

const Vocabulary = () => {
    const [vocabulary, setVocabulary] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [wordInput, setWordInput] = useState('');
    const [meanings, setMeanings] = useState([{ meaning: '', partOfSpeech: '' }]);

    const openAddModal = () => setIsModalOpen(true);
    const closeAddModal = () => {
        setIsModalOpen(false);
        setWordInput('');
        setMeanings([{ meaning: '', partOfSpeech: '' }]);
    };

    const addMeaning = () => {
        setMeanings([...meanings, { meaning: '', partOfSpeech: '' }]);
    };

    const handleMeaningChange = (index, field, value) => {
        const updatedMeanings = meanings.map((meaning, i) =>
            i === index ? { ...meaning, [field]: value } : meaning
        );
        setMeanings(updatedMeanings);
    };

    const submitWord = () => {
        if (wordInput && meanings.every(m => m.meaning && m.partOfSpeech)) {
            const newVocab = {
                word: wordInput,
                meanings: meanings.map(m => `${m.meaning} (${m.partOfSpeech})`).join(', '),
            };
            setVocabulary([...vocabulary, newVocab]);
            closeAddModal();
        } else {
            alert('모든 필드를 입력해주세요.');
        }
    };

    const deleteVocabulary = (index) => {
        setVocabulary(vocabulary.filter((_, i) => i !== index));
    };

    return (
        <div style={{ padding: '20px', backgroundColor: '#f8f9fa' }}>
            <header>
                <h1>단어장 이름</h1>
                <section className="vocab-buttons">
                    <button onClick={() => window.location.href = 'flashcard.html'}>플래시 카드</button>
                    <button onClick={() => window.location.href = 'OXquiz.html'}>O/X</button>
                    <button onClick={() => window.location.href = 'fillin.html'}>빈칸 채우기</button>
                    <button className="add-button" onClick={openAddModal}>+</button>
                </section>
            </header>
            <main>
                <section className="voca-list">
                    {vocabulary.map((item, index) => (
                        <div key={index} className="voca-item">
                            <div>
                                <span className="checkbox">
                                    <input type="checkbox" className="learning-checkbox" />
                                </span>
                                <strong>{item.word}</strong> - {item.meanings}
                            </div>
                            <div>
                                <button onClick={() => alert("구현 예정")}>수정</button>
                                <button onClick={() => deleteVocabulary(index)}>삭제</button>
                            </div>
                        </div>
                    ))}
                </section>
            </main>

            {isModalOpen && (
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={closeAddModal}>&times;</span>
                        <h2>단어 추가</h2>
                        <input
                            type="text"
                            placeholder="단어 입력"
                            value={wordInput}
                            onChange={(e) => setWordInput(e.target.value)}
                            style={{ marginBottom: '5px' }}
                            required
                        />
                        <div id="meanings-container">
                            {meanings.map((meaning, index) => (
                                <div key={index} className="meaning-item">
                                    <input
                                        type="text"
                                        placeholder="뜻 입력"
                                        value={meaning.meaning}
                                        onChange={(e) => handleMeaningChange(index, 'meaning', e.target.value)}
                                        required
                                    />
                                    <select
                                        value={meaning.partOfSpeech}
                                        onChange={(e) => handleMeaningChange(index, 'partOfSpeech', e.target.value)}
                                    >
                                        <option value="">품사 선택</option>
                                        <option value="명사">명사</option>
                                        <option value="형용사">형용사</option>
                                        <option value="동사">동사</option>
                                        <option value="부사">부사</option>
                                    </select>
                                </div>
                            ))}
                        </div>
                        <button className="add-meaning" onClick={addMeaning}>+</button>
                        <button className="add-button" onClick={submitWord}>제출</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Vocabulary;
