import React, {useState, useEffect} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import Modal from './Modal';
import '../styles/vocabulary.css';

const POS = Object.freeze({
    NOUN: "명사",
    PRONOUN: "대명사",
    VERB: "동사",
    ADJECTIVE: "형용사",
    ADVERB: "부사",
    ARTICLE: "관사",
    PREPOSITION: "전치사",
    CONJUNCTION: "접속사",
    INTERJECTION: "감탄사"
});

const getPOSKey = (value) => {
    return Object.keys(POS).find((key) => POS[key] === value) || "";
};

const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

const Vocabulary = ({isDarkMode, vocabId, userId, words, fetchVocabData, fetchJson}) => {
    const [isAddModalOpen, setIsAddModalOpen] = useState(false);
    const [wordInput, setWordInput] = useState('');
    const [addingDefs, setAddingDefs] = useState([{definition: '', type: ''}]);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [editingWord, setEditingWord] = useState(null);
    const [editWordIndex, setEditWordIndex] = useState(null);
    const [removeDefs, setRemoveDefs] = useState([]);

    useEffect(() => {
        console.log("removeDefs", removeDefs);
    }, [removeDefs]);

    useEffect(() => {
        console.log("editingW0rd", editingWord);
    }, [editingWord]);

    useEffect(() => {
        fetchVocabData(userId);
    }, [vocabId]);

    const openAddModal = () => setIsAddModalOpen(true);
    const closeAddModal = () => {
        setIsAddModalOpen(false);
        setWordInput('');
        setAddingDefs([{definition: '', type: ''}]);
    };

    const openEditModal = (word) => {
        setEditingWord(word);
        setEditWordIndex(words.findIndex((w) => w.expression === word.expression));
        setIsEditModalOpen(true);
    };

    const closeEditModal = () => {
        setIsEditModalOpen(false);
        setEditingWord(null);
        setEditWordIndex(null);
    };

    const addAddDef = () => setAddingDefs((prev) => [...prev, {definition: '', type: ''}]);
    const addEditDef = () => setEditingWord(prev => ({
        ...prev, // 기존 상태를 유지
        defs: [...prev.defs, {definition: '', type: ''}] // defs 배열에 새 def 추가
    }));

    const removeAddingDef = (index) => {
        const addingData = [...addingDefs];
        addingData.pop(index);
        console.log("addingData", addingData);
        setAddingDefs(addingData);
    };

    const removeEditingDef = (index) => {
        const editingData = [...editingWord.defs];
        setRemoveDefs((prev) => [...prev, editingData.pop(index)]);
        console.log("editingData", editingData);
        setEditingWord((prev) => ({...prev, defs: editingData}));
    };

    const handleAddingDefChange = (index, definition) => {
        const updatedDefs = [...addingDefs];
        updatedDefs[index].definition = definition;
        console.log("addingDefs", updatedDefs);
        setAddingDefs(updatedDefs);
    };

    const handleAddingTypeChange = (index, type) => {
        const updatedDefs = [...addingDefs];
        updatedDefs[index].type = type;
        console.log("addingDefs", updatedDefs);
        setAddingDefs(updatedDefs);
    };

    const handleEditingDefChange = (index, def, definition) => {
        const updatedDefs = [...editingWord.defs];
        def.definition = definition;
        updatedDefs[index] = def;
        console.log("editingDefs", updatedDefs);
        setEditingWord((prev) => ({...prev, defs: updatedDefs}));
    };

    const handleEditingTypeChange = (index, def, type) => {
        const updatedDefs = [...editingWord.defs];
        def.type = type;
        updatedDefs[index] = def;
        console.log("editingDefs", updatedDefs);
        setEditingWord((prev) => ({...prev, defs: updatedDefs}));
    };

    const submitAddWord = async () => {
        const trimmedWord = wordInput.trim();
        console.log("addingDefs", addingDefs);
        if (!trimmedWord) {
            alert('단어를 입력해주세요.');
            return;
        }

        if (trimmedWord.length > 50) {
            alert('단어는 50자를 초과할 수 없습니다.');
            return;
        }

        const specialChars = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>?~`]/;
        if (specialChars.test(trimmedWord)) {
            alert('단어에는 특수문자를 포함할 수 없습니다.');
            return;
        }

        if (!addingDefs.every((def) => def.definition.trim() && def.type)) {
            alert('모든 의미와 품사를 입력해주세요.');
            return;
        }

        try {
            const responseWord = await fetchJson(`/api/words/${vocabId}`, 'POST', {
                expression: trimmedWord
            });

            addingDefs.map(async (def) => {
                await fetchJson(`/api/defs/${responseWord.data.wordId}`, 'POST', {
                    definition: def.definition, type: def.type
                });
            });
            await delay(50);
            await fetchVocabData(userId);
        } catch (error) {
            if (error.status && error.status === 409) {
                alert('중복된 단어입니다.');
                console.error('중복된 단어입니다', error);
            }
            alert('단어 추가 중 문제가 발생했습니다.');
            console.error('단어 추가 실패:', error);
        }

        closeAddModal();
    };

    const submitEditWord = async () => {
        const trimmedWord = editingWord.expression.trim();
        if (!trimmedWord) {
            alert('단어를 입력해주세요.');
            return;
        }

        if (trimmedWord.length > 50) {
            alert('단어는 50자를 초과할 수 없습니다.');
            return;
        }

        const specialChars = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>?~`]/;
        if (specialChars.test(trimmedWord)) {
            alert('단어에는 특수문자를 포함할 수 없습니다.');
            return;
        }

        if (!editingWord.defs.every((def) => def.definition.trim() && def.type)) {
            alert('모든 의미와 품사를 입력해주세요.');
            return;
        }

        try {
            await fetchJson(`/api/words/${editingWord.wordId}`, 'PATCH', {
                expression: trimmedWord
            });

            editingWord.defs.map(async (def) => {
                if (def.defId == null) {
                    //뜻을 새로 추가하는 경우 => POST 요청
                    await fetchJson(`/api/defs/${editingWord.wordId}`, 'POST', {
                        definition: def.definition, type: def.type
                    });
                } else {
                    //이미 있는 뜻을 수정하는 경우 => PATCH 요청
                    await fetchJson('/api/defs/' + def.defId, 'PATCH', {
                        definition: def.definition, type: def.type
                    });
                }
            });
            if (removeDefs && removeDefs.length > 0) {
                removeDefs.map(async (def) => {
                    const removeResponse = await fetchJson('/api/defs/' + def.defId, 'DELETE');
                    console.log('responseRemove', removeResponse);
                });
            }
            await fetchVocabData(userId);
            setRemoveDefs([]);
        } catch (error) {
            console.error('단어 추가 실패:', error);
            if (error.status && error.status === 409) {
                alert('중복된 단어입니다.');
                console.error('중복된 단어입니다', error);
            } else {
                alert('단어 추가 중 문제가 발생했습니다.');
                console.error('단어 추가 중 문제가 발생했습니다', error);
            }
        }
        closeEditModal();
    };

    const deleteWord = async (word) => {
        console.log(`deleteWord()`, word);
        if (window.confirm('정말로 삭제하시겠습니까?')) {
            try {
                await fetchJson(`/api/words/${word.wordId}`, 'delete');
                await fetchVocabData(userId);
            } catch (e) {
                alert("삭제에 실패했습니다.");
                console.error("단어 삭제 실패", e);
            }
        }
    };

    const getDifficultyClass = (word) => {
        if (!(words.length > 0))
            return '';
        const diff = word.stats.diff;
        if (diff == null || ((diff < 0.69) && (diff > 0.45)))
            return 'vocab-item--difficulty-medium';
        else if (diff <= 0.45)
            return 'vocab-item--difficulty-easy';
        else
            return 'vocab-item--difficulty-hard';
    };

    const toggleIsLearned = async (word) => {
        try {
            if(word.stats.isLearned){
                await fetchJson('/api/stats/' + word.wordId, 'PATCH',
                    {isLearned: 0});
            } else{
                await fetchJson('/api/stats/' + word.wordId, 'PATCH',
                    {isLearned: 1});
            }
            await fetchVocabData(userId);
        } catch (e) {
            alert("통계 정보 업데이트 실패");
            console.error("통계 정보 업데이트 실패", e);
        }
    }

    return (
        <div
            className="vocabulary-container"
            style={styles.vocabularyContainer}
        >
            <div style={styles.addButtonContainer}>
                <button className="custom-button" onClick={openAddModal}>Add</button>
            </div>

            <main>
                <div style={styles.vocaList}>
                    {words && words.length > 0 ? (words.map((word) => (
                        <div
                            key={word.wordId}
                            className={`vocaItem ${word.stats.isLearned ? 'selectedVocaItem' : ''} ${getDifficultyClass(word)}` || 0.5}
                            style={styles.vocaItem}
                        >
                            <div style={styles.vocaItemContent}
                                 onClick={() => toggleIsLearned(word)}>
                                <div style={styles.wordContent}>
                                    <div style={styles.word}>{word.expression}</div>
                                    <div style={styles.meaning}>
                                        {word.defs.map((def) => (
                                            <span key={def.defId}>
                                                {def.definition} ({POS[def.type]});{" "}
                                            </span>
                                        ))}
                                    </div>
                                </div>
                            </div>
                            <div className="action-buttons">
                                <button className="action-button" onClick={() => openEditModal(word)}>수정</button>
                                <button className="action-button" onClick={() => deleteWord(word)}>삭제</button>
                            </div>
                        </div>
                    ))) : ("단어가 없습니다.")}
                </div>
            </main>

            {isAddModalOpen && (
                <Modal title="단어 추가" onClose={closeAddModal}>
                    <input
                        style={{...styles.commonInput, ...styles.input}}
                        type="text"
                        placeholder="단어 입력"
                        value={wordInput}
                        onChange={(e) => setWordInput(e.target.value)}
                    />
                    <div style={styles.meaningsContainer}>
                        {addingDefs.map((def, index) => (
                            <div key={index} style={styles.meaningItem}>
                                <input
                                    style={{...styles.commonInput, ...styles.input}}
                                    type="text"
                                    placeholder="뜻 입력"
                                    value={def.definition}
                                    onChange={(item) => handleAddingDefChange(index, item.target.value)}
                                />
                                <select
                                    style={{
                                        ...styles.commonInput,
                                        ...styles.selectBox,
                                        backgroundColor: isDarkMode ? '#5a5b5d' : '#fff',
                                        color: isDarkMode ? '#e4e6eb' : '#000',
                                        borderColor: isDarkMode ? '#3a3b3c' : '#ddd'
                                    }}
                                    value={POS[def.type]}
                                    onChange={(item) => handleAddingTypeChange(index, getPOSKey(item.target.value))}
                                >
                                    <option value="">품사 선택</option>
                                    {Object.values(POS).map((value) => (
                                        <option key={value} value={value}>
                                            {value}
                                        </option>
                                    ))}
                                </select>
                                {addingDefs.length > 1 && (
                                    <button
                                        className="custom-button"
                                        onClick={() => removeAddingDef(index)}
                                    >
                                        -
                                    </button>
                                )}
                            </div>
                        ))}
                    </div>
                    <button
                        className="add-meaning-button"
                        onClick={addAddDef}
                    >+
                    </button>
                    <button className="custom-button" onClick={submitAddWord}>추가</button>
                </Modal>
            )}

            {isEditModalOpen && (
                <Modal title="단어 수정" onClose={closeEditModal}>
                    <input
                        style={{...styles.commonInput, ...styles.input}}
                        type="text"
                        placeholder="단어 입력"
                        value={editingWord.expression}
                        onChange={(e) => setEditingWord((prev) => ({...prev, expression: e.target.value}))}
                    />
                    <div style={styles.meaningsContainer}>
                        {editingWord.defs.map((def, index) => (
                            <div key={index} style={styles.meaningItem}>
                                <input
                                    style={{...styles.commonInput, ...styles.input}}
                                    type="text"
                                    placeholder="뜻 입력"
                                    value={def.definition || ""}
                                    onChange={(e) => handleEditingDefChange(index, def, e.target.value)}
                                />
                                <select
                                    style={{
                                        ...styles.commonInput,
                                        ...styles.selectBox,
                                        backgroundColor: isDarkMode ? '#5a5b5d' : '#fff',
                                        color: isDarkMode ? '#e4e6eb' : '#000',
                                        borderColor: isDarkMode ? '#3a3b3c' : '#ddd'
                                    }}
                                    value={POS[def.type]}
                                    onChange={(e) => handleEditingTypeChange(index, def, getPOSKey(e.target.value))}
                                >
                                    <option value="">품사 선택</option>
                                    {Object.values(POS).map((value) => (
                                        <option key={value} value={value}>
                                            {value}
                                        </option>
                                    ))}
                                </select>
                                {editingWord.defs.length > 1 && (
                                    <button
                                        className="custom-button"
                                        onClick={() => removeEditingDef(index)}
                                    >
                                        -
                                    </button>
                                )}
                            </div>
                        ))}
                    </div>
                    <button
                        className="add-meaning-button"
                        onClick={addEditDef}
                    >
                        +
                    </button>
                    <button className="custom-button" onClick={submitEditWord}>수정</button>
                </Modal>
            )}
        </div>
    );
};

const styles = {
    vocabularyContainer: {
        fontFamily: 'TTHakgyoansimEunhasuR',
        padding: '20px',
        maxHeight: '700px',
        overflowY: 'auto',
        overflowX: 'hidden'
    },
    addButtonContainer: {
        display: 'flex',
        justifyContent: 'flex-end',
        padding: '10px',
        marginBottom: '20px'
    },
    vocaList: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: '10px'
    },
    vocaItem: {
        width: '85%',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        fontSize: '18px',
        padding: '15px',
        borderBottom: '1px solid #ddd',
        backgroundColor: 'rgba(249, 249, 249, 0.7)',
        borderRadius: '8px',
        boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
        position: 'relative',
        overflow: 'hidden',
        margin: '3px'
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