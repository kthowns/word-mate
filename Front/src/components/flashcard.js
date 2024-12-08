import React, {useState, useEffect} from 'react';
import {useParams} from 'react-router-dom';

const Flashcard = ({ isDarkMode, vocabId, fetchVocabData, fetchJson, words, userId }) => {
    const [totalQuestions, setTotalQuestions] = useState(0);
    const [currentQuestion, setCurrentQuestion] = useState(0);
    const [isQuizEnd, setIsQuizEnd] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [isFirstQuestion, setIsFirstQuestion] = useState(true);
    const [isLastQuestion, setIsLastQuestion] = useState(false);
    const [isWordHide, setIsWordHide] = useState(false);
    const [isDefHide, setIsDefHide] = useState(true);
    const [hideText, setHideText] = useState("단어 가리기");
    const [isAllVisible, setIsAllVisible] = useState(false);
    const [visibleText, setVisibleText] = useState("가리기");

    const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

    const fetchData = async () => {
        await fetchVocabData(userId);
    };

    useEffect(() => {
        setIsLoading(true);
        setIsQuizEnd(false);
        fetchData();
        setIsLoading(false);
        setTotalQuestions(words.length);
        setCurrentQuestion(0);
    }, [vocabId]);

    useEffect(() => {
        setIsFirstQuestion(currentQuestion === 0);
        setIsLastQuestion(currentQuestion === totalQuestions - 1);

        console.log("currentQuestion", currentQuestion);
        console.log("totalQuestion", totalQuestions);
    }, [currentQuestion, totalQuestions]);

    useEffect(() => {
        if (isWordHide) {
            setHideText("뜻 가리기");
        } else {
            setHideText("단어 가리기");
        }
    }, [isDefHide, isWordHide])

    useEffect(() => {
        console.log("isAllVisible", isAllVisible);
        isAllVisible ? setVisibleText("가리기") : setVisibleText("답 보기");
    }, [isAllVisible]);

    const prevButton = async () => {
        setIsAllVisible(false);
        if (currentQuestion > 0)
            setCurrentQuestion((prev) => prev - 1);
    };

    const nextButton = async () => {
        setIsAllVisible(false);
        if (currentQuestion + 1 < totalQuestions)
            setCurrentQuestion((prev) => prev + 1);
    };

    const hideToggle = () => {
        setIsWordHide(!isWordHide);
        setIsDefHide(!isDefHide);
    };

    return (
        <div style={{
            fontFamily: 'TTHakgyoansimEunhasuR',
            backgroundColor: isDarkMode ? '#242526' : '#f8f9fa',
            display: 'flex',
            flexDirection: 'column',
            height: '70vh',
            position: 'relative',
            padding: '20px',
            color: isDarkMode ? '#e4e6eb' : '#000',
            borderRadius: '20px',
            boxShadow: isDarkMode
                ? '0 0 20px rgba(0,0,0,0.3)'
                : '0 0 20px rgba(0,0,0,0.1)',
        }}>
            <header style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                borderBottom: `2px solid ${isDarkMode ? '#404040' : '#ddd'}`,
                padding: '10px',
                height: '10%',
                color: isDarkMode ? '#e4e6eb' : '#000'
            }}>
                <p></p>
                <p>{currentQuestion + 1 + "/" + totalQuestions}</p>
                <p></p>
            </header>

            <main style={{
                flex: 1,
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center'
            }}>
                <div
                    style={{
                        width: '500px',
                        height: '300px',
                        backgroundColor: isDarkMode ? '#3a3b3c' : '#fff',
                        borderRadius: '8px',
                        boxShadow: isDarkMode ? '0 2px 4px rgba(0,0,0,0.2)' : '0 2px 4px rgba(0,0,0,0.1)',
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center',
                        textAlign: 'center',
                        position: 'relative',
                        color: isDarkMode ? '#e4e6eb' : '#000'
                    }}
                >
                    {words && words.length > 0 ? (
                        <section style={{
                            flexDirection: 'column',
                            alignItems: 'center',
                            gap: '20px'
                        }}>
                            <button
                                className="flashcard-button"
                                onClick={hideToggle}
                                style={{
                                    position: 'absolute',
                                    top: '10px',
                                    left: '10px'
                                }}
                            >
                                {hideText}
                            </button>
                            <p style={{
                                backgroundColor:
                                    isWordHide && !isAllVisible ? (
                                        isDarkMode ? "#e4e6eb" : "black"
                                    ) : "transparent"
                            }}>
                                {words[currentQuestion].expression || "로딩 중"}</p>
                            <p style={{
                                backgroundColor:
                                    isDefHide && !isAllVisible ? (
                                        isDarkMode ? "#e4e6eb" : "black"
                                    ) : "transparent"
                            }}>
                                {words[currentQuestion].defs.map((def) => (
                                    <span key={def.defId}>
                                    {def.definition} ({def.type});{" "}
                                </span>
                                )) || "로딩 중"}</p>
                        </section>
                    ) : (
                        "단어가 없습니다."
                    )}
                </div>
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
                    onClick={prevButton}
                    disabled={isQuizEnd || isLoading || isFirstQuestion}
                >
                    이전
                </button>

                <button
                    onClick={() => { setIsAllVisible(!isAllVisible); }}
                >
                    {visibleText}
                </button>

                <button
                    onClick={nextButton}
                    disabled={isQuizEnd || isLoading || isLastQuestion}
                >
                    다음
                </button>
            </footer>
        </div>
    );
};

export default Flashcard;
