import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Modal from './Modal';
import '../styles/vocabulary.css';

const Vocabulary = ({ vocabularies, onUpdateVocabulary }) => {
    const { id } = useParams();
    const [vocabulary, setVocabulary] = useState([]);
    const [selectedItems, setSelectedItems] = useState(new Set());
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [wordInput, setWordInput] = useState('');
    const [meanings, setMeanings] = useState([{ meaning: '', partOfSpeech: '' }]);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [editWordInput, setEditWordInput] = useState('');
    const [editMeanings, setEditMeanings] = useState([{ meaning: '', partOfSpeech: '' }]);
    const [editIndex, setEditIndex] = useState(null);
    const [vocabularyTitle, setVocabularyTitle] = useState('');
    
    useEffect(() => {
        // vocab 정보를 받아오는 API 호출
        const fetchVocabulary = async () => {
            try {
                const response = await fetch(`http://127.0.0.1:8080/api/vocabs/detail?vocab_id=${id}`); // vocab 정보 가져오기
                const data = await response.json();
                console.log("vocab API response:", data);
    
                // API 응답 구조에 맞게 데이터 처리
                if (data.status === 200 && data.data) {
                    const currentVocab = data.data;
    
                    // vocab의 title 설정
                    setVocabularyTitle(currentVocab.title);
    
                    // words 정보를 가져오기 위한 추가 API 호출
                    const wordsResponse = await fetch(`http://127.0.0.1:8080/api/words/all?vocab_id=${id}`);
                    const wordsData = await wordsResponse.json();
                    console.log("words API response:", wordsData);
    
                    if (wordsData.status === 200 && Array.isArray(wordsData.data)) {
                        const wordsWithDefs = await Promise.all(
                            wordsData.data.map(async (word) => {
                                // 각 word에 대한 defs 정보를 가져오는 API 호출
                                const defsResponse = await fetch(`http://127.0.0.1:8080/api/defs/all?word_id=${word.wordId}`);
                                const defsData = await defsResponse.json();
                                console.log("defsData for word:", defsData);
    
                                // 단어에 defs를 추가하여 반환
                                return {
                                    ...word,
                                    defs: defsData.status === 200 ? defsData.data || [] : [],
                                };
                            })
                        );
    
                        // 단어와 defs 정보가 결합된 단어 목록을 vocabulary에 저장
                        setVocabulary(wordsWithDefs);
                    } else {
                        console.error('Words data fetch failed:', wordsData.message);
                    }
                } else {
                    console.error('Vocab data fetch failed:', data.message);
                }
            } catch (error) {
                console.error('데이터를 불러오는 데 실패했습니다:', error);
            }
        };
    
        fetchVocabulary(); // API 호출
    }, [id]); // `id`가 변경될 때마다 호출
    


    const toggleSelection = (index) => {
        const newSelected = new Set(selectedItems);
        if (newSelected.has(index)) {
            newSelected.delete(index);
        } else {
            newSelected.add(index);
        }
        setSelectedItems(newSelected);
    };

    const navigate = useNavigate();
    const openAddModal = () => setIsModalOpen(true);
    const closeAddModal = () => {
        setIsModalOpen(false);
        setWordInput('');
        setMeanings([{ meaning: '', partOfSpeech: '' }]);
    };

    const openEditModal = (index) => {
        const wordToEdit = vocabulary[index];
        setEditWordInput(wordToEdit.word);
        setEditMeanings(
            wordToEdit.meanings.split(', ').map((m) => {
                const [meaning, partOfSpeech] = m.split(' (');
                return { meaning, partOfSpeech: partOfSpeech.slice(0, -1) };
            })
        );
        setEditIndex(index);
        setIsEditModalOpen(true);
    };

    const closeEditModal = () => {
        setIsEditModalOpen(false);
        setEditWordInput('');
        setEditMeanings([{ meaning: '', partOfSpeech: '' }]);
        setEditIndex(null);
    };

    const addMeaning = () => setMeanings([...meanings, { meaning: '', partOfSpeech: '' }]);
    const addEditMeaning = () => setEditMeanings([...editMeanings, { meaning: '', partOfSpeech: '' }]);

    const removeMeaning = (index) => {
        if (meanings.length > 1) {
            setMeanings(meanings.filter((_, i) => i !== index));
        }
    };

    const removeEditMeaning = (index) => {
        if (editMeanings.length > 1) {
            setEditMeanings(editMeanings.filter((_, i) => i !== index));
        }
    };

    const handleMeaningChange = (index, field, value) => {
        const updatedMeanings = meanings.map((meaning, i) =>
            i === index ? { ...meaning, [field]: value } : meaning
        );
        setMeanings(updatedMeanings);
    };

    const handleEditMeaningChange = (index, field, value) => {
        const updatedMeanings = editMeanings.map((meaning, i) =>
            i === index ? { ...meaning, [field]: value } : meaning
        );
        setEditMeanings(updatedMeanings);
    };

    const submitWord = () => {
        if (wordInput && meanings.every((m) => m.meaning && m.partOfSpeech)) {
            const newWord = {
                id: Date.now(),
                word: wordInput,
                meanings: meanings.map((m) => `${m.meaning} (${m.partOfSpeech})`).join(', ')
            };
            
            const updatedVocabulary = [...vocabulary, newWord];
            setVocabulary(updatedVocabulary);
            onUpdateVocabulary(Number(id), updatedVocabulary);
            closeAddModal();
        } else {
            alert('모든 필드를 입력해주세요.');
        }
    };

    const submitEditWord = () => {
        if (editWordInput && editMeanings.every((m) => m.meaning && m.partOfSpeech)) {
            const updatedVocabulary = [...vocabulary];
            updatedVocabulary[editIndex] = {
                ...updatedVocabulary[editIndex],
                word: editWordInput,
                meanings: editMeanings.map((m) => `${m.meaning} (${m.partOfSpeech})`).join(', '),
            };
            setVocabulary(updatedVocabulary);
            onUpdateVocabulary(Number(id), updatedVocabulary);
            closeEditModal();
        } else {
            alert('모든 필드를 입력해주세요.');
        }
    };

    const deleteVocabulary = (index) => {
        if (window.confirm('정말로 삭제하시겠습니까?')) {
            const updatedVocabulary = vocabulary.filter((_, i) => i !== index);
            setVocabulary(updatedVocabulary);
            onUpdateVocabulary(Number(id), updatedVocabulary);
        }
    };

    return (
        <div style={styles.vocabularyContainer}>
            <header style={styles.header}>
                <div style={styles.headerLeft}>
                    <button 
                        className="back-button"
                        onClick={() => navigate('/')}
                    >
                        ←
                    </button>
                </div>
                <h1 style={styles.title}>{vocabularyTitle}</h1>
                <div style={styles.vocabButtons}>
                    <button className="custom-button" onClick={() => navigate(`/flashcard/${id}`)}>플래시 카드</button>
                    <button className="custom-button" onClick={() => navigate(`/OXquiz/${id}`)}>O/X</button>
                    <button className="custom-button" onClick={() => navigate(`/fillin/${id}`)}>빈칸 채우기</button>
                    <button className="custom-button" onClick={openAddModal}>+</button>
                </div>
            </header>

            <main>
            <div style={styles.vocaList}>
                {vocabulary.map((item, index) => (
                    <div
                        key={item.id || index}
                        className={`vocaItem ${selectedItems.has(index) ? 'selectedVocaItem' : ''}`}
                        style={styles.vocaItem}
                        onDoubleClick={() => toggleSelection(index)}
                    >
                        <div style={styles.vocaItemContent}>
                            <div style={styles.checkbox}>
                                <input
                                    type="checkbox"
                                    style={styles.checkboxInput}
                                    checked={selectedItems.has(index)}
                                    onChange={() => toggleSelection(index)}
                                />
                            </div>
                            <div style={styles.wordContent}>
                                <div style={styles.word}>{item.word}</div>
                                <div style={styles.meaning}>
                                    {item.defs.map((def, defIndex) => (
                                        <div key={defIndex}>
                                            <div>{def.definition}</div>
                                            <div>{def.type}</div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </div>
                        <div className="action-buttons">
                            <button className="action-button" onClick={() => openEditModal(index)}>수정</button>
                            <button className="action-button" onClick={() => deleteVocabulary(index)}>삭제</button>
                        </div>
                    </div>
                ))}
            </div>
            </main>

            {isModalOpen && (
                <Modal title="단어 추가" onClose={closeAddModal}>
                    <input
                        style={{...styles.commonInput, ...styles.input}}
                        type="text"
                        placeholder="단어 입력"
                        value={wordInput}
                        onChange={(e) => setWordInput(e.target.value)}
                    />
                    <div style={styles.meaningsContainer}>
                        {meanings.map((meaning, index) => (
                            <div key={index} style={styles.meaningItem}>
                                <input
                                    style={{...styles.commonInput, ...styles.input}}
                                    type="text"
                                    placeholder="뜻 입력"
                                    value={meaning.meaning}
                                    onChange={(e) => handleMeaningChange(index, 'meaning', e.target.value)}
                                />
                                <select
                                    style={{...styles.commonInput, ...styles.selectBox}}
                                    value={meaning.partOfSpeech}
                                    onChange={(e) => handleMeaningChange(index, 'partOfSpeech', e.target.value)}
                                >
                                    <option value="">품사 선택</option>
                                    <option value="명사">명사</option>
                                    <option value="형용사">형용사</option>
                                    <option value="동사">동사</option>
                                    <option value="부사">부사</option>
                                </select>
                                {meanings.length > 1 && (
                                    <button 
                                        className="custom-button"
                                        onClick={() => removeMeaning(index)}
                                    >
                                        -
                                    </button>
                                )}
                            </div>
                        ))}
                    </div>
                    <button className="custom-button" onClick={addMeaning}>+</button>
                    <button className="custom-button" onClick={submitWord}>추가</button>
                </Modal>
            )}

            {isEditModalOpen && (
                <Modal title="단어 수정" onClose={closeEditModal}>
                    <input
                        style={{...styles.commonInput, ...styles.input}}
                        type="text"
                        placeholder="단어 입력"
                        value={editWordInput}
                        onChange={(e) => setEditWordInput(e.target.value)}
                    />
                    <div style={styles.meaningsContainer}>
                        {editMeanings.map((meaning, index) => (
                            <div key={index} style={styles.meaningItem}>
                                <input
                                    style={{...styles.commonInput, ...styles.input}}
                                    type="text"
                                    placeholder="뜻 입력"
                                    value={meaning.meaning}
                                    onChange={(e) => handleEditMeaningChange(index, 'meaning', e.target.value)}
                                />
                                <select
                                    style={{...styles.commonInput, ...styles.selectBox}}
                                    value={meaning.partOfSpeech}
                                    onChange={(e) => handleEditMeaningChange(index, 'partOfSpeech', e.target.value)}
                                >
                                    <option value="">품사 선택</option>
                                    <option value="명사">명사</option>
                                    <option value="형용사">형용사</option>
                                    <option value="동사">동사</option>
                                    <option value="부사">부사</option>
                                </select>
                                {editMeanings.length > 1 && (
                                    <button 
                                        className="custom-button"
                                        onClick={() => removeEditMeaning(index)}
                                    >
                                        -
                                    </button>
                                )}
                            </div>
                        ))}
                    </div>
                    <button className="custom-button" style={styles.addMeaningButton} onClick={addEditMeaning}>+</button>
                    <button className="custom-button" onClick={submitEditWord}>수정</button>
                </Modal>
            )}
        </div>
    );
};

const styles = {
    vocabularyContainer: {
        fontFamily: 'TTHakgyoansimEunhasuR',
        padding: '20px'
    },
    header: {
        borderTop: '1px solid #ddd',
        borderBottom: '1px solid #ddd',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '50px',
        position: 'relative',
        height: '60px'
    },
    headerLeft: {
        display: 'flex',
        alignItems: 'center',
        gap: '20px',
        position: 'absolute',
        left: 0,
        zIndex: 1
    },
    vocabButtons: {
        display: 'flex',
        gap: '10px',
        position: 'absolute',
        right: 0,
        zIndex: 1
    },
    vocaList: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: '10px'
    },
    vocaItem: {
        width:'85%',
        alignItems: 'center',
        display: 'flex',
        justifyContent: 'space-between',
        fontSize: '18px',
        padding: '15px',
        borderBottom: '1px solid #ddd',
        backgroundColor: 'rgba(249, 249, 249, 0.7)',
        borderRadius: '8px',
        boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
        position: 'relative',
        overflow: 'hidden',
        margin:'3px'
    },
    vocaItemContent: {
        display: 'flex',
        alignItems: 'center',
        gap: '10px',
        cursor: 'pointer',
        flex: 1,
        marginLeft: '15px'
    },
    wordContent: {
        flex: 1,
        marginLeft: '20px',
    },
    word: {
        fontSize: '24px',
        fontWeight: '500',
        marginBottom: '8px'
    },
    meaning: {
        fontSize: '18px',
        color: '#666'
    },
    checkbox: {
        marginRight: '10px',
    },
    checkboxInput: {
        accentColor: '#a9c6f8',
        cursor: 'pointer'
    },
    actionButtons: {
        display: 'flex',
        gap: '10px',
        opacity: 0,
        transition: 'opacity 0.3s ease'
    },
    commonInput: {
        padding: '8px 12px',
        border: '1px solid #ddd',
        borderRadius: '4px',
        fontSize: '14px'
    },
    input: {
        width: '80%',
        marginBottom: '10px'
    },
    meaningsContainer: {
        display: 'flex',
        flexDirection: 'column',
        gap: '10px',
        marginBottom: '20px'
    },
    meaningItem: {
        display: 'flex',
        gap: '10px'
    },
    selectBox: {
        fontFamily: 'TTHakgyoansimEunhasuR',
        width: '120px',
        backgroundColor: '#fff',
        cursor: 'pointer',
        outline: 'none'
    },
    addMeaningButton: {
        width: '40px',
        height: '40px',
        padding: '0',
        borderRadius: '50%',
        marginRight: '10px'
    },
    selectedVocaItem: {
        borderLeft: '8px solid transparent', 
        borderImage: 'linear-gradient(to bottom, #c5d8ff, #a9c6f8) 1'
    },
    title: {
        fontSize: '40px',
        fontWeight: 'normal',
        position: 'absolute',
        left: '50%',
        transform: 'translateX(-50%)',
        margin: 0,
        width: '100%',
        textAlign: 'center'
    }
};

export default Vocabulary;