/*성장일지 화면*/
import React, {
  useState,
  useContext,
  useEffect,
  useCallback,
  useMemo,
} from "react";
import axios from "axios";
import AddgrowthIcon from "../images/svg/add-diary.svg";
import { ChildContext } from "../index";
import CreateGrowthModal from "../components/growthComponent/CreateGrowthModal";
import styled from "styled-components";
import upBtn from "../images/svg/up-arrow.svg";
import downBtn from "../images/svg/down-arrow.svg";
import ImgCard from "../components/aacComponent/ImgCard";
import NotFoundImg from "../images/svg/error-icon.svg";

const s3BaseUrl = process.env.REACT_APP_S3_BASE_URL; // 환경 변수에서 AWS S3 BASE URL 가져오기

const GrowthDropdownContainer = styled.div`
  position: relative;
  display: flex;
  margin-top: 0px;
  width: 100%;
  max-width: 350px;
`;

const GrowthDropdownToggle = styled.button`
  width: 350px;
  height: 48px;
  padding: 5px 18px;
  border: solid 1px #ffce1b;
  cursor: pointer;
  background-color: var(--click-yellow);
  display: flex;
  justify-content: flex-start;
  align-items: center;
  font-family: "Pretendard-Regular";
  font-size: 16px;
`;

const GrowthDropdownMenu = styled.ul`
  position: absolute;
  top: 100%;
  z-index: 1000;
  display: block;
  width: 348px;
  padding: 0;
  margin: -2px 0 0;
  font-size: 16px;
  text-align: left;
  list-style: none;
  background-color: var(--white);
  border: solid 1px var(--dark-yellow);
  max-height: ${({ $isopen }) => ($isopen ? "500px" : "0")};
  overflow: hidden;
  opacity: ${({ $isopen }) => ($isopen ? "1" : "0")};
  transition: max-height 0.3s ease, opacity 0.3s ease;
`;

const GrowthCustomArrow = styled.span`
  width: 18px;
  height: 18px;
  margin-left: auto;
  background-size: cover;
  transition: transform 0.3s ease;
  background-image: url(${({ $isopen }) => ($isopen ? upBtn : downBtn)});
  transform: ${({ $isopen }) => ($isopen ? "rotate(180deg)" : "rotate(0)")};
`;

const GrowthDropdownItem = styled.li`
  display: flex;
  align-items: center;
  padding: 8px 20px;
  font-family: "Pretendard-Regular";
  clear: both;
  font-weight: 400;
  text-align: inherit;
  white-space: nowrap;
  background: none;
  border: 0;
  padding: 0px 18px;
  cursor: pointer;
  width: 350px;
  height: 48px;
  color: var(--black);
  &:hover {
    background-color: var(--click-yellow);
  }
  ${({ selected }) =>
    selected &&
    `
    background-color: var(--white);
    font-family: "Pretendard-Bold";
  `}
`;

const GrowthRoot = styled.div``;

//얘가 다른 페이지에선 root 역할하는건데 content로 대체 ㅇㅇ
const GrowthContent = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  transform: translateX(290px);
  min-height: 100vh;
  width: calc(100% - 745px);
  max-width: 820px;
  margin-top: 60px;
  margin-left: 405px;
  margin-right: 405px;
  overflow-y: auto;
  h1 {
    font-family: "Pretendard-Bold";
    font-size: 32px;
    width: 100%;
  }
`;

const AddDiaryButton = styled.button`
  background-color: transparent;
  border: none;
  cursor: pointer;
  width: 157px;
  height: 44px;
  margin-left: -5px;
`;

const GrowthTopBtn = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  max-width: 820px;
  padding: 0px;
  margin-left: 0px;
  margin-top: 42px;
  margin-bottom: 42px;
`;

const DiaryList = styled.div`
  width: 820px;
  font-family: "Pretendard-Bold";
`;
//이게 헤더 바로 위 컨테이너. 다이어리 크기 바꾸려면 얘를 바꿔야함
const DiaryItem = styled.div`
  width: 820px;
  background-color: #ffef85;
  margin-bottom: 12px;
  border-radius: 7px;
  overflow: hidden;
  transition: height 0.5s ease;
`;

const DiaryHeader = styled.div`
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 18px;
  cursor: pointer;
  font-family: "Pretendard-Bold";
  font-size: 16px;
  background-color: #ffef85;
`;

const DiaryHeaderSpan = styled.span`
  flex-grow: 1;
  text-align: left;
  margin-left: 10px;
  font-size: 16px;
  font-family: "Pretendard-Bold";
`;

const DiaryCustomArrow = styled.span`
  width: 18px;
  height: 18px;
  margin-left: auto;
  background-size: cover;
  transition: transform 0.3s ease;
  background-image: url(${({ $isopen }) => ($isopen ? upBtn : downBtn)});
  transform: ${({ $isopen }) => ($isopen ? "rotate(180deg)" : "rotate(0)")};
`;

const ExpandButton = styled.button`
  background-color: #ffef85;
  border: none;
  cursor: pointer;
  font-family: "Pretendard-Regular";
  font-size: 16px;
`;
//얘가 list 내부 컨테이너
const DiaryDetails = styled.div`
  height: 515px;
  background-color: rgba(247, 246, 245, 1);
  padding-top: 40px;
  padding-left: 40px;
  border-radius: 0 0 8px 8px;
  overflow-y: auto;
  &::-webkit-scrollbar {
    width: 10px;
  }
  &::-webkit-scrollbar-track {
    background: rgba(247, 246, 245, 1);
    border-radius: 3px;
  }
  &::-webkit-scrollbar-thumb {
    background-color: #999999;
    border-radius: 10px;
    border: 2px solid rgba(247, 246, 245, 1);
  }
  
  p {
    font-family: "Pretendard-Regular";
  }
`;

const DiaryImages = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  overflow-x: hidden;
  margin-top: 20px;
`;
const DiaryImagesDetail = styled.div`
  margin-right: 20px;
  width: 220px;
  height: 264px;
  box-sizing: border-box;
`;

const DiaryTopDiv = styled.div`
  display: flex;
  gap: 20px;
  margin-bottom: 40px;
`;

const DiaryTitle = styled.h2`
  font-family: "Pretendard-Bold";
  font-size: 32px;
`;

const DiaryInfo = styled.p`
  font-family: "Pretendard-Regular";
  margin-top: 17px;
  font-size: 16px;
`;

const CardNumCount = styled.div`
  display: flex;
  flex-direction: column;

  p {
    font-family: "Pretendard-Regular";
    gap: 20px;
    font-size: 16px;
    margin-bottom: 30px;
  }
`;
const LearningP = styled.p`
  font-family: "Pretendard-Regular";
  margin-top: 10px;
  font-size: 16px;
`;

const sortDiaries = (diaries, option) => {
  console.log("Diaries before sorting:", diaries);
  console.log("Sorting option:", option);
  const sortedDiaries = diaries.sort((a, b) => {
    if (option === "최신순(default)") {
      return new Date(b.creationTime) - new Date(a.creationTime);
    } else if (option === "오래된순") {
      return new Date(a.creationTime) - new Date(b.creationTime);
    }
    return diaries;
  });
  console.log("Sorted diaries:", sortedDiaries);
  return sortedDiaries;
};

const Growth = () => {
  const { selectedChild, setSelectedChild } = useContext(ChildContext);
  const [expandedDiary, setExpandedDiary] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [diaries, setDiaries] = useState([]);
  const [isopen, setIsOpen] = useState(false);
  const [selected, setSelected] = useState("최신순(default)");

  useEffect(() => {
    const storedSelectedChild = JSON.parse(
      localStorage.getItem("selectedChild")
    );
    if (storedSelectedChild) {
      setSelectedChild(storedSelectedChild);
      console.log("선택된 아동 코드:", storedSelectedChild?.childCode);
    }
  }, [setSelectedChild]);

  const fetchGrowth = useCallback(async () => {
    if (!selectedChild) {
      console.error("아동을 선택하세요!");
      return;
    }

    const userId = localStorage.getItem("userId");
    const childCode = selectedChild.childCode;

    try {
      console.log("Request data:", { userId, childCode });
      const response = await axios.get(
        `${process.env.REACT_APP_SERVER_IP}/growth/diary`,
        {
          params: {
            userId,
            childCode,
          },
        }
      );

      console.log("Response data:", response.data);

      const diariesData = response.data || [];

      console.log("Received data structure:", diariesData);

      const normalizedDiaries = diariesData.map((diary) => ({
        imageCardNum: diary.imageCardNum || 0,
        videoCardNum: diary.videoCardNum || 0,
        dailyLearningLogDtoList: diary.dailyLearningLogDtoList || [],
        creationTime: diary.creationTime.replace(/:/g, ".") || "", //오류 나면 replace 없애기
        userName: diary.userName || "",
      }));

      console.log("Normalized 성장일지 데이터:", normalizedDiaries);
      setDiaries(sortDiaries(normalizedDiaries, selected));
    } catch (error) {
      console.error("성장일지 불러오기 실패:", error);
      setDiaries([]);
    }
  }, [selectedChild, selected]);

  useEffect(() => {
    if (selectedChild) {
      console.log("선택된 아동 코드:", selectedChild.childCode);
      fetchGrowth();
    }
  }, [selectedChild, fetchGrowth]);

  //정렬용 코드
  useEffect(() => {
    setDiaries((prevDiaries) => sortDiaries([...prevDiaries], selected));
  }, [selected]);

  const sortedDiaries = useMemo(
    () => sortDiaries([...diaries], selected),
    [diaries, selected]
  );

  const toggleExpand = (index) => {
    setExpandedDiary(expandedDiary === index ? null : index);
  };

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const handleDiaryCreated = () => {
    fetchGrowth();
  };

  const toggleDropdown = () => setIsOpen(!isopen);

  const handleSelect = (option) => {
    setSelected(option);
    setIsOpen(false);
    setDiaries((prevDiaries) => sortDiaries([...prevDiaries], option));
  };

  const options = ["최신순(default)", "오래된순"];

  return (
    <GrowthRoot>
      <GrowthContent>
        <h1>성장일지</h1>
        <GrowthTopBtn>
          <AddDiaryButton onClick={openModal}>
            <img
              src={AddgrowthIcon}
              alt="일지 추가하기"
              className="AddgrowthIcon"
            />
          </AddDiaryButton>
          <GrowthDropdownContainer>
            <GrowthDropdownToggle onClick={toggleDropdown}>
              {selected} <GrowthCustomArrow $isopen={isopen} />
            </GrowthDropdownToggle>
            <GrowthDropdownMenu $isopen={isopen}>
              {options.map((option) => (
                <GrowthDropdownItem
                  key={option}
                  onClick={() => handleSelect(option)}
                  selected={selected === option}
                >
                  {option}
                </GrowthDropdownItem>
              ))}
            </GrowthDropdownMenu>
          </GrowthDropdownContainer>
        </GrowthTopBtn>
        <DiaryList>
          {sortedDiaries.length > 0 ? (
            sortedDiaries.map((diary, index) => (
              <DiaryItem key={index}>
                <DiaryHeader onClick={() => toggleExpand(index)}>
                  <DiaryCustomArrow $isopen={expandedDiary === index} />
                  <DiaryHeaderSpan>
                    {diary.creationTime} {selectedChild.name}의 성장일지
                  </DiaryHeaderSpan>
                  <ExpandButton>{diary.userName} </ExpandButton>
                </DiaryHeader>
                {expandedDiary === index && (
                    <DiaryDetails>
                      <DiaryTopDiv>
                        <DiaryTitle>{`${selectedChild.name}의 성장일지`}</DiaryTitle>
                        <DiaryInfo>{`${diary.creationTime} / ${diary.userName}`}</DiaryInfo>
                      </DiaryTopDiv>

                      {diary.dailyLearningLogDtoList !== undefined ? (
                          <>
                            <CardNumCount>
                              <p>
                                오늘은 {diary.imageCardNum + diary.videoCardNum}개의
                                카드가 생성되었어요
                              </p>
                              <p>
                                AAC 카드: {diary.imageCardNum}개 / 비디오 카드:
                                {diary.videoCardNum}개
                              </p>
                            </CardNumCount>

                            {(() => {
                              const topCard = diary.dailyLearningLogDtoList.reduce(
                                  (prev, current) => (current.clickCnt > prev.clickCnt ? current : prev),
                                  { cardName: "N/A", color: "000000", image: "", clickCnt: -Infinity } // 초기값
                              );
                              return (
                                  <>
                                    <LearningP>오늘 가장 많이 클릭한 카드:</LearningP>
                                    <DiaryImages>
                                      <DiaryImagesDetail>
                                        <ImgCard
                                            imgName={topCard.cardName}
                                            imgColor={`#${topCard.color}`}
                                            imgSrc={topCard.image ? `${s3BaseUrl}${encodeURI(topCard.image)}` : NotFoundImg}
                                            style={{
                                              width: "100px",
                                              height: "105px",
                                              border: "3px solid blue" // 별도 강조 스타일
                                            }}
                                        />
                                      </DiaryImagesDetail>
                                    </DiaryImages>
                                    <br></br>
                                    <LearningP>오늘 학습한 카드 목록:</LearningP>
                                    <DiaryImages>
                                      {diary.dailyLearningLogDtoList.map((card, cardIndex) => (
                                          <DiaryImagesDetail key={cardIndex}>
                                            <ImgCard
                                                imgName={card.cardName}
                                                imgColor={`#${card.color}`}
                                                imgSrc={card.image ? `${s3BaseUrl}${encodeURI(card.image)}` : NotFoundImg}
                                                style={{
                                                  width: "100px",
                                                  height: "105px",
                                                  border: card.cardName === topCard.cardName ? "2px solid red" : "none" // 가장 많이 학습한 카드 강조
                                                }}
                                            />
                                          </DiaryImagesDetail>
                                      ))}
                                    </DiaryImages>
                                  </>
                              );
                            })()}
                          </>
                      ) : (
                          <p>학습된 카드가 없습니다.</p>
                      )}
                    </DiaryDetails>

                )}
              </DiaryItem>
            ))
          ) : (
            <p></p>
          )}
        </DiaryList>
      </GrowthContent>
      {isModalOpen && (
        <CreateGrowthModal
          closeModal={closeModal}
          isModalOpen={isModalOpen}
          onDiaryCreated={handleDiaryCreated}
        />
      )}
    </GrowthRoot>
  );
};

export default Growth;
