import React, {useState, useEffect} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import Modal from './Modal';
import '../styles/vocabulary.css';

async function fetchJson(url, method = 'GET', body = null) {
    const headers = {'Content-Type': 'application/json'};
    const options = {method, headers};

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);
    const data = await response.json();
    if (data.status === 200) {
        return data;
    } else {
        console.error("Failed to load " + url);
        return [];
    }
}

const Vocabulary = ({isDarkMode, vocabId}) => {
    const [words, setWords] = useState([]);
    const [selectedItems, setSelectedItems] = useState([]);
    const [isAddModalOpen, setIsAddModalOpen] = useState(false);
    const [wordInput, setWordInput] = useState('');
    const [addingDefs, setAddingDefs] = useState([]);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [editWordInput, setEditWordInput] = useState('');
    const [edittingDefs, setEdittingDefs] = useState([{definition: '', type: ''}]);
    const [editIndex, setEditIndex] = useState(null);
    const [vocabularyTitle, setVocabularyTitle] = useState('');

    const fetchVocabData = async () => {
        try {
            const wordResponse = await fetch(`/api/words/all?vocab_id=${vocabId}`);
            const wordData = await wordResponse.json();
            if (wordData.status === 200) {
                if (wordData.data.length === 0) { //빈 단어장
                    setWords([{expression: "", defs: {definition: ""}}]);
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

                    let statsData = await fetch(`/api/stats/detail?word_id=${word.wordId}`)
                        .then((res) => res.json())
                        .then((data) => (data.status == 200 ? data.data : []))
                        .catch((err) => {
                            console.error("통계 정보 불러오기 실패", err);
                            return [];
                        });

                    const diffsData = await fetch(`/api/stats/diff?word_id=${word.wordId}`)
                        .then((res) => res.json())
                        .then((data) => (data.status == 200 ? data.data : []))
                        .catch((err) => {
                            console.error("난이도 불러오기 실패", err);
                            return;
                        })

                    return {...word, defs: defsData, stats: {...statsData, diff: diffsData}};
                }));

                setWords(wordsWithDefs);
            } else {
                console.error("단어 정보 불러오기 실패");
            }
        } catch (error) {
            console.error('Error fetching vocab data:', error);
        }
    };

    useEffect(() => {
        fetchVocabData();
    }, [vocabId]);

    const navigate = useNavigate();
    const openAddModal = () => setIsAddModalOpen(true);
    const closeAddModal = () => {
        setIsAddModalOpen(false);
        setWordInput('');
        setAddingDefs([{definition: '', type: ''}]);
    };

    const openEditModal = (word) => {
        setEditWordInput(word.expression);
        setEdittingDefs(
            word.defs.map((def) => {
                const definition = def.definition;
                const type = def.type;
                return {definition: definition, type: type};
            })
        );
        setEditIndex(words.findIndex((w) => w.expression === word.expression));
        setIsEditModalOpen(true);
    };

    const closeEditModal = () => {
        setIsEditModalOpen(false);
        setEditWordInput('');
        setEdittingDefs([{definition: '', type: ''}]);
        setEditIndex(null);
    };

    const addAddDef = () => setAddingDefs([...addingDefs, {definition: '', type: ''}]);
    const addEditDef = () => setEdittingDefs([...edittingDefs, {definition: '', type: ''}]);

    const removeAddingDef = (index) => {
        if (defs.length > 1) {
            setAddingDefs(addingDefs.filter((_, i) => i !== index));
        }
    };

    const removeEdittingDef = (index) => {
        if (edittingDefs.length > 1) {
            setEdittingDefs(edittingDefs.filter((_, i) => i !== index));
        }
    };

    const handleAddingDefChange = (index, definition) => {
        const updatedDefs = addingDefs.map((def, i) =>
            i === index ? {...def, definition: definition} : definition
        );
        setAddingDefs(updatedDefs);
    };

    const handleAddingTypeChange = (index, type) => {
        const updatedDefs = addingDefs.map((def, i) =>
            i === index ? {...def, type: type} : type
        );
        setAddingDefs(updatedDefs);
    };

    const handleEdittingDefChange = (index, definition) => {
        const updatedDefs = edittingDefs.map((def, i) =>
            i === index ? {...def, definition: definition} : definition
        );
        setEdittingDefs(updatedDefs);
    };

    const handleEdittingTypeChange = (index, type) => {
        const updatedDefs = edittingDefs.map((def, i) =>
            i === index ? {...def, type: type} : type
        );
        setEdittingDefs(updatedDefs);
    };

    const submitAddWord = async () => {
        const trimmedWord = wordInput.trim();
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
                const responseDef = await fetchJson(`/api/defs/${responseWord.data.wordId}`, 'POST',{
                    definition: def.definition, type: def.type
                });
                if(responseDef.status !== 200){
                    if(responseDef.status === 409){
                        alert('중복된 뜻입니다');
                    }else{
                        alert('뜻 추가에 실패했습니다.');
                    }
                }
            });

            if (responseWord.status === 200 && responseDef) {
                await fetchVocabData();
                closeAddModal();
            } else if (responseWord.status === 409) {
                alert('중복된 단어입니다.');
            } else {
                alert('단어 추가에 실패했습니다.');
            }
        } catch (error) {
            console.error('단어 추가 실패:', error);
            alert('단어 추가 중 문제가 발생했습니다.');
        }

        closeAddModal();
    };

    const submitEditWord = async () => {
        const trimmedWord = editWordInput.trim();
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

        if (!edittingDefs.every((def) => def.definition.trim() && def.type)) {
            alert('모든 의미와 품사를 입력해주세요.');
            return;
        }

        try {
            const response = await fetchJson(`/api/defs/${words[editIndex].wordId}`, 'PATCH', {
                word: trimmedWord,
                defs: edittingDefs.map((def) => ({
                    definition: def.definition.trim(),
                    type: def.type
                })),
            });

            if (response.status === 200) {
                fetchVocabData();
                closeAddModal();
            } else if (response.status == 409) {
                alert('중복된 단어입니다.');
            } else {
                alert('단어 수정에 실패했습니다.');
            }
        } catch (error) {
            console.error('단어 수정 실패:', error);
            alert('단어 수정 중 문제가 발생했습니다.');
        }

        closeEditModal();
    };

    const deleteWord = async (word) => {
        console.log(`deleteWord()`, word);
        if (window.confirm('정말로 삭제하시겠습니까?')) {
            await fetchJson(`/api/words/${word.wordId}`, 'delete');
            await fetchVocabData();
        }
    };

    const getDifficultyClass = (word) => {
        console.log("getDifficultyClass", word);
        if (word.stats.diff == null)
            return 'vocab-item--difficulty-medium';
        if (word.stats.diff > 0.60)
            return 'vocab-item--difficulty-hard';
        else if (word.stats.diff > 45)
            return 'vocab-item--difficulty-medium';
        return 'vocab-item--difficulty-easy';
    };

    const toggleSelectedItem = (wordId) => {
        if(!selectedItems.includes(wordId))
            setSelectedItems((prev) => [...prev, wordId]);
        else
            setSelectedItems((prev) => prev.filter((item) => item !== wordId));
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
                    {words.map((word) => (
                        <div
                            key={word.wordId}
                            className={`vocaItem ${selectedItems.includes(word.wordId) ? 'selectedVocaItem' : ''} ${getDifficultyClass(word)}`}
                            style={styles.vocaItem}
                            onClick={() => toggleSelectedItem(word.wordId)}
                        >
                            <div style={styles.vocaItemContent}>
                                <div style={styles.checkbox}>
                                    <input
                                        type="checkbox"
                                        style={styles.checkboxInput}
                                        checked={selectedItems.includes(word.wordId)}
                                    />
                                </div>
                                <div style={styles.wordContent}>
                                    <div style={styles.word}>{word.expression}</div>
                                    <div style={styles.meaning}>
                                        {word.defs.map((def) => (
                                            <span key={def.defId}>
                                                    {def.definition} ({def.type});{" "}
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
                    ))}
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
                                    value={def.type}
                                    onChange={(item) => handleAddingTypeChange(index, item.target.value)}
                                >
                                    <option value="">품사 선택</option>
                                    <option value="명사">명사</option>
                                    <option value="형용사">형용사</option>
                                    <option value="동사">동사</option>
                                    <option value="부사">부사</option>
                                </select>
                                {addingDefs.length > 1 && (
                                    <button
                                        className="custom-button"
                                        onClick={() => removeAddingDef(def)}
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
                        value={editWordInput}
                        onChange={(e) => setEditWordInput(e.target.value)}
                    />
                    <div style={styles.meaningsContainer}>
                        {edittingDefs.map((def, index) => (
                            <div key={index} style={styles.meaningItem}>
                                <input
                                    style={{...styles.commonInput, ...styles.input}}
                                    type="text"
                                    placeholder="뜻 입력"
                                    value={def.definition}
                                    onChange={(e) => handleEdittingDefChange(index, e.target.value)}
                                />
                                <select
                                    style={{
                                        ...styles.commonInput,
                                        ...styles.selectBox,
                                        backgroundColor: isDarkMode ? '#5a5b5d' : '#fff',
                                        color: isDarkMode ? '#e4e6eb' : '#000',
                                        borderColor: isDarkMode ? '#3a3b3c' : '#ddd'
                                    }}
                                    value={def.type}
                                    onChange={(e) => handleEdittingTypeChange(index, e.target.value)}
                                >
                                    <option value="">품사 선택</option>
                                    <option value="명사">명사</option>
                                    <option value="형용사">형용사</option>
                                    <option value="동사">동사</option>
                                    <option value="부사">부사</option>
                                </select>
                                {edittingDefs.length > 1 && (
                                    <button
                                        className="custom-button"
                                        onClick={() => removeEdittingDef(index)}
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