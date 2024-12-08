import React, {useState, useEffect} from 'react';
import {BrowserRouter as Router, Routes, Route, Link, useNavigate} from 'react-router-dom';
import Modal from './components/Modal';
import './styles/App.css';
import logo from './resources/logo.png';
import logoDark from './resources/logo_night.png';
import Flashcard from './components/flashcard';
import OXQuiz from './components/OXquiz';
import FillIn from './components/fillin';
import Vocabulary from './components/vocabulary';

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
        if (data.status === 409) {
            const error = new Error("Duplicated data from server");
            error.status = 409;
            console.error("Duplicated data from server", data)
            throw error;
        } else if (data.status === 404) {
            const error = new Error("Not found data from server");
            error.status = 404;
            console.error("Not found data from server", data);
            throw error;
        } else if (data.status === 401) {
            const error = new Error("Unauthorized");
            error.status = 401;
            console.error("Unauthorized", data);
            throw error;
        } else {
            const error = new Error("Internal Server error");
            error.status = 500;
            console.error("Internal Server error", data)
            throw error;
        }
        return [];
    }
}

function App() {
    const [vocabularies, setVocabularies] = useState([]);
    const [userId, setUserId] = useState(0);
    const [showAddModal, setShowAddModal] = useState(false);
    const [showEditModal, setShowEditModal] = useState(false);
    const [newVocab, setNewVocab] = useState({title: '', description: ''});
    const [editingVocab, setEditingVocab] = useState(null);
    const [isDarkMode, setIsDarkMode] = useState(() => {
        const savedMode = localStorage.getItem('darkMode');
        return savedMode ? JSON.parse(savedMode) : false;
    });
    const [expandedVocabId, setExpandedVocabId] = useState(null);
    const [currentView, setCurrentView] = useState(null); // 'vocabulary', 'flashcard', 'oxquiz', 'fillin'
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isLogged, setIsLogged] = useState(false);

    const handleLogin = async () => {
        if (!username || !password) {
            alert('아이디와 비밀번호를 모두 입력해주세요.');
            return;
        }
        try {
            const response = await fetchJson('/api/login', 'POST',
                {username: username, password: password});
            if (response.status === 200) {
                setUserId(response.data.userId);
                setIsLogged(true);
            }
        } catch (e) {
            if (e.status === 404) {
                alert("유효하지 않은 ID");
                console.log("유효하지 않은 ID", e);
            } else if (e.status === 401) {
                alert("유효하지 않은 비밀번호");
                console.log("유효하지 않은 비밀번호", e);
            } else {
                alert("로그인 실패");
                console.log("로그인 실패", e);
            }
        }
    };

    const fetchVocabData = async () => {
        try {
            const vocabData = await fetchJson(`/api/vocabs/all?user_id=${userId}`);
            if (vocabData.status === 200) {
                const vocabsWithWords = await Promise.all(
                    vocabData.data.map(async (vocab) => {
                        const lrData = await fetch(`/api/stats/lr?vocab_id=${vocab.vocabId}`)
                            .then((res) => res.json())
                            .then((data) => (data.status === 200 ? data.data : []))
                            .catch((err) => {
                                console.error("통계 정보 불러오기 실패", err);
                                return [];
                            });
                        return {...vocab, words: await fetchWordData(vocab.vocabId), lr: lrData};
                    })
                );
                setVocabularies(vocabsWithWords);
            } else {
                console.error("단어장 정보 불러오기 실패");
                setVocabularies([]);
            }
        } catch (error) {
            console.error('Error fetching vocab data:', error);
        }
    };

    const fetchWordData = async (vocabId) => {
        try {
            const wordResponse = await fetch(`/api/words/all?vocab_id=${vocabId}`);
            const wordData = await wordResponse.json();
            if (wordData.status === 200) {
                if (wordData.data.length === 0) { //빈 단어장
                    return [];
                } else {
                    const wordsWithDefs = await Promise.all(wordData.data.map(async (word) => {
                        const defsData = await fetch(`/api/defs/all?word_id=${word.wordId}`)
                            .then((res) => res.json())
                            .then((data) => (
                                data.status === 200 ? data.data : [])
                            )
                            .catch((err) => {
                                console.error("뜻 정보 불러오기 실패", err);
                                return [];
                            });

                        const statsData = await fetch(`/api/stats/detail?word_id=${word.wordId}`)
                            .then((res) => res.json())
                            .then((data) => (
                                data.status === 200 ? data.data : [])
                            )
                            .catch((err) => {
                                console.error("통계 정보 불러오기 실패", err);
                                return [];
                            });

                        const diffData = await fetch(`/api/stats/diff?word_id=${word.wordId}`)
                            .then((res) => res.json())
                            .then((data) => (data.status === 200 ? data.data : []))
                            .catch((err) => {
                                console.error("통계 정보 불러오기 실패", err);
                                return [];
                            });

                        return {...word, defs: defsData, stats: {...statsData, diff: diffData}};
                    }));
                    return wordsWithDefs;
                }
            } else {
                console.error("단어 정보 불러오기 실패");
            }
        } catch (error) {
            console.error('Error fetching vocab data:', error);
        }
    };

    useEffect(() => {
        fetchVocabData();
    }, [userId]);

    useEffect(() => {
        console.log(vocabularies);
    }, [vocabularies]);

    useEffect(() => {
        localStorage.setItem('darkMode', JSON.stringify(isDarkMode));
        document.body.className = isDarkMode ? 'dark-mode' : '';
    }, [isDarkMode]);

    const toggleDarkMode = () => {
        setIsDarkMode(!isDarkMode);
    };

    const openAddModal = () => setShowAddModal(true);
    const closeAddModal = () => {
        setShowAddModal(false);
        setNewVocab({title: '', description: ''});
    }

    const openEditModal = (vocab) => {
        setEditingVocab(vocab);
        setShowEditModal(true);
    };
    const closeEditModal = () => {
        setEditingVocab(null);
        setShowEditModal(false);
    };

    const handleSubmitVocabulary = async () => {
        if (newVocab.title) {
            const vocabToAdd = {title: newVocab.title, description: newVocab.description};
            try {
                await fetchJson(`/api/vocabs/${userId}`, 'POST', vocabToAdd);
                await fetchVocabData();
            } catch (e) {
                if (e.status && e.status === 409) {
                    alert("중복된 단어장입니다");
                    console.error("중복된 단어장입니다", e);
                } else {
                    alert('단어 추가 중 문제가 발생했습니다.');
                    console.error("단어 추가 중 문제가 발생했습니다", e);
                }
            }
            setNewVocab({title: '', description: ''});
            closeAddModal();
        } else {
            alert('제목을 입력해 주세요.');
        }
    };

    const handleUpdateVocabulary = async () => {
        if (editingVocab) {
            const updatedVocab = {title: newVocab.title, description: newVocab.description};
            try {
                await fetchJson(`/api/vocabs/${editingVocab.vocabId}`, 'PATCH', updatedVocab);
                await fetchVocabData();
            } catch (e) {
                if (e.status && e.status === 409) {
                    alert("중복된 단어장입니다");
                    console.error("중복된 단어장입니다", e);
                } else {
                    alert("단어 수정에 실패했습니다.");
                    console.error("단어 수정에 실패했습니다.", e);
                }
            }
        }
        closeEditModal();
    };

    const deleteVocabulary = async (vocabId) => {
        try {
            await fetchJson(`/api/vocabs/${vocabId}`, 'DELETE');
        } catch (e) {
            alert("단어장 삭제 실패");
            console.error("단어장 삭제 실패", e);
            return;
        }
        await fetchVocabData();
    };

    const toggleVocabExpand = (vocabId, view = null) => {
        if (expandedVocabId === vocabId && currentView === view) {
            setExpandedVocabId(null);
            setCurrentView(null);
        } else {
            setExpandedVocabId(vocabId);
            setCurrentView(view);
        }
    };

    const returnToVocabList = (vocabId) => {
        setExpandedVocabId(vocabId);
        setCurrentView(null);
    };

    const renderVocabContent = (vocab) => {
        if (expandedVocabId !== vocab.vocabId) return null;

        switch (currentView) {
            case 'flashcard':
                return <Flashcard
                    words={vocabularies.find((vocab) => expandedVocabId === vocab.vocabId)?.words || []}
                    isDarkMode={isDarkMode}
                    vocabId={vocab.vocabId}
                    userId={userId}
                    fetchVocabData={fetchVocabData}
                    fetchJson={fetchJson}
                />;
            case 'oxquiz':
                return <OXQuiz
                    words={vocabularies.find((vocab) => expandedVocabId === vocab.vocabId)?.words || []}
                    isDarkMode={isDarkMode}
                    vocabId={vocab.vocabId}
                    userId={userId}
                    fetchVocabData={fetchVocabData}
                    fetchJson={fetchJson}
                />;
            case 'fillin':
                return <FillIn
                    words={vocabularies.find((vocab) => expandedVocabId === vocab.vocabId)?.words || []}
                    isDarkMode={isDarkMode}
                    vocabId={vocab.vocabId}
                    userId={userId}
                    fetchVocabData={fetchVocabData}
                    fetchJson={fetchJson}
                />;
            default:
                return (
                    <Vocabulary
                        words={vocabularies.find((vocab) => expandedVocabId === vocab.vocabId)?.words || []}
                        isDarkMode={isDarkMode}
                        vocabId={vocab.vocabId}
                        userId={userId}
                        fetchVocabData={fetchVocabData}
                        fetchJson={fetchJson}
                    />
                );
        }
    };

    return (
        <Router>
            <div className={`container ${isDarkMode ? 'dark-mode' : ''}`}>
                {isLogged ? (
                    <div>
                        <header>
                            <div className="header-left">
                                <img src={isDarkMode ? logoDark : logo} alt="Logo" className="logo"/>
                                <button className="add-button" onClick={openAddModal}>Add</button>
                                <button
                                    className={`mode-toggle-button ${isDarkMode ? 'dark' : 'light'}`}
                                    onClick={toggleDarkMode}
                                >
                                    {isDarkMode ? 'NIGHT' : 'DAY'}
                                </button>
                            </div>
                            <div className="learning-rate">
                                <p>평균 학습률 : {(() => {
                                    let totalLr = 0.0;
                                    let totalVocab = vocabularies.length;
                                    vocabularies.forEach((vocab) => {
                                        vocab.words.length === 0 ? totalVocab-- : totalLr += vocab.lr;
                                    })
                                    return (totalLr/totalVocab).toFixed(2);
                                })()}</p>
                                <p>퀴즈 정답률 : {(() => {
                                    let totalCorrectCount = 0.0;
                                    let totalIncorrectCount = 0.0;
                                    vocabularies.forEach((vocab) => {
                                        if(vocab.words.length > 0){
                                            vocab.words.forEach((word) => {
                                                totalCorrectCount += word.stats.correctCount;
                                                totalIncorrectCount += word.stats.incorrectCount;
                                            })
                                        }
                                        console.log("correctCount", totalCorrectCount, totalIncorrectCount);
                                    })
                                    return (totalCorrectCount/(totalCorrectCount+totalIncorrectCount)).toFixed(2);
                                })()}</p>
                                <p>총 단어 개수 : {(() => {
                                    let totalWord = 0;
                                    vocabularies.forEach((vocab) => {
                                        totalWord += vocab.words.length;
                                    })
                                    return totalWord;
                                })()}</p>
                            </div>
                        </header>

                        <main>
                            <section className="vocab-list">
                                {vocabularies.map((vocab) => (
                                    <div key={vocab.vocabId} className="vocab-item">
                                        <div
                                            className="vocab-header"
                                            onClick={() => toggleVocabExpand(vocab.vocabId)}
                                        >
                                            <h2>{vocab.title} ({vocab.wordCount} 단어)</h2>
                                            <p className="description">{vocab.description}</p>
                                            <p style={
                                                {textAlign: "right", marginTop: "5px", fontSize: "17px"}
                                            }>학습률 : {vocab.lr ? vocab.lr.toFixed(2) : "0"}</p>
                                        </div>
                                        <div className="vocab-buttons-container">
                                            <div className="vocab-buttons">
                                                <button
                                                    className="vocab-button"
                                                    onClick={() => toggleVocabExpand(vocab.vocabId, 'flashcard')}
                                                >
                                                    플래시 카드
                                                </button>
                                                <button
                                                    className="vocab-button"
                                                    onClick={() => toggleVocabExpand(vocab.vocabId, 'oxquiz')}
                                                >
                                                    O/X
                                                </button>
                                                <button
                                                    className="vocab-button"
                                                    onClick={() => toggleVocabExpand(vocab.vocabId, 'fillin')}
                                                >
                                                    빈칸 채우기
                                                </button>
                                            </div>
                                            <div className="action-buttons">
                                                <button
                                                    className="vocab-button edit-button"
                                                    onClick={() => openEditModal(vocab)}
                                                >
                                                    수정
                                                </button>
                                                <button
                                                    className="vocab-button delete-button"
                                                    onClick={() => deleteVocabulary(vocab.vocabId)}
                                                >
                                                    삭제
                                                </button>
                                            </div>
                                        </div>
                                        <div className="vocab-expand-section">
                                            <button
                                                className="expand-button"
                                                onClick={() => {
                                                    if (expandedVocabId === vocab.vocabId) {
                                                        setExpandedVocabId(null);
                                                        setCurrentView(null);
                                                    } else {
                                                        setExpandedVocabId(vocab.vocabId);
                                                        setCurrentView(null);
                                                    }
                                                }}
                                            >
                                                {expandedVocabId === vocab.vocabId ? '접기' : '단어목록 펼치기'}
                                            </button>
                                        </div>
                                        {renderVocabContent(vocab)}
                                    </div>
                                ))}
                            </section>
                        </main>
                    </div>) : (
                    <div style={styles_login.container}>
                        <header style={styles_login.header}>로그인</header>
                        <main style={styles_login.main}>
                            <input
                                type="text"
                                placeholder="아이디 입력"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                style={styles_login.input}
                                required
                            />
                            <input
                                type="password"
                                placeholder="비밀번호 입력"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                style={styles_login.input}
                                required
                            />
                            <button onClick={handleLogin} style={styles_login.button}>로그인</button>
                        </main>
                    </div>
                )}
                {showAddModal && (
                    <Modal title="단어장 추가" onClose={closeAddModal}>
                        <label>제목:</label>
                        <input
                            type="text"
                            value={newVocab.title}
                            onChange={(e) => setNewVocab({...newVocab, title: e.target.value})}
                            className="input-field"
                        />
                        <label>설명:</label>
                        <textarea
                            value={newVocab.description}
                            onChange={(e) => setNewVocab({...newVocab, description: e.target.value})}
                            className="textarea-field"
                        />
                        <button onClick={handleSubmitVocabulary} className="submit-button">
                            제출
                        </button>
                    </Modal>
                )}

                {showEditModal && (
                    <Modal title="단어장 수정" onClose={closeEditModal}>
                        <label>제목:</label>
                        <input
                            type="text"
                            value={newVocab.title}
                            onChange={(e) => setNewVocab({...newVocab, title: e.target.value})}
                            className="input-field"
                        />
                        <label>설명:</label>
                        <textarea
                            value={newVocab.description}
                            onChange={(e) => setNewVocab({...newVocab, description: e.target.value})}
                            className="textarea-field"
                        />
                        <button onClick={handleUpdateVocabulary} className="submit-button">
                            수정
                        </button>
                    </Modal>
                )}
            </div>
        </Router>
    );
}

const styles_login = {
    container: {
        margin: 0,
        padding: 0,
        backgroundColor: '#f8f9fa',
        display: 'flex',
        flexDirection: 'column',
        height: '100vh',
        justifyContent: 'center',
        alignItems: 'center',
    },
    header: {
        fontSize: '36px',
        marginBottom: '30px',
        color: '#333',
    },
    main: {
        backgroundColor: 'white',
        border: '1px solid #ccc',
        borderRadius: '8px',
        padding: '40px',
        width: '400px',
        textAlign: 'center',
    },
    input: {
        width: '100%',
        height: '40px',
        marginBottom: '15px',
        padding: '10px',
        border: '1px solid #ccc',
        borderRadius: '5px',
        fontSize: '16px',
    },
    button: {
        width: '100%',
        height: '45px',
        backgroundColor: '#007bff',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        fontSize: '18px',
    },
    buttonHover: {
        backgroundColor: '#0056b3',
    },
};

export default App;
