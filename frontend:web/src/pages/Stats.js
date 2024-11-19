import React, { useState, useEffect, useMemo } from 'react';
import axios from "axios";
import { useLocation } from "react-router-dom";
import PersonalBarChart from "../components/chartComponent/PersonalBarChart";
import PieChart from "../components/chartComponent/PieChart";
import ImgCard from "../components/aacComponent/ImgCard";
import ScrollAnimation from "../components/AnimationComponent/ScrollAnimation";

import "../styles/Stats.css";
import styled from "styled-components";
import upBtn from "../images/svg/up-arrow.svg";
import downBtn from "../images/svg/down-arrow.svg";
import NotFoundImg from "../images/svg/error-icon.svg"; //404 그림
const s3BaseUrl = process.env.REACT_APP_S3_BASE_URL; // 환경 변수에서 AWS S3 BASE URL 가져오기

const StatsDropdownContainer = styled.div`
  position: relative;
  display: flex;
  margin-top: 0px;
  width: 100%;
  max-width: 310px;
`;

const StatsDropdownToggle = styled.button`
  width: 310px;
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

const StatsDropdownMenu = styled.ul`
  position: absolute;
  top: 100%;
  z-index: 1000;
  display: block;
  width: 308px;
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

const StatsCustomArrow = styled.span`
  width: 18px;
  height: 18px;
  margin-left: auto;
  background-size: cover;
  transition: transform 0.3s ease;
  background-image: url(${({ $isopen }) => ($isopen ? upBtn : downBtn)});
  transform: ${({ $isopen }) => ($isopen ? "rotate(180deg)" : "rotate(0)")};
`;

const StatsDropdownItem = styled.li`
  display: flex;
  align-items: center;
  padding-left: 18px;
  font-family: "Pretendard-Regular";
  clear: both;
  font-weight: 400;
  text-align: inherit;
  white-space: nowrap;
  background: none;
  border: 0;
  cursor: pointer;
  width: 310px;
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

const StatsImagesDetail = styled.div`
  margin-top: 20px;
  margin-left: 0px;
  width: 220px;
  height: 264px;
  box-sizing: border-box;
`;

function Stats() {
  const location = useLocation();
  const { userId, childCode, childName } = location.state || {};
  const [childStats, setChildStats] = useState([[], [], []]);
  const [isopen, setIsOpen] = useState(false);
  const [selected, setSelected] = useState("일간");

  const toggleDropdown = () => setIsOpen(!isopen);

  const options = ["일간", "주간", "월간"];
  const handleSelect = (option) => {
    setSelected(option);
    setIsOpen(false);
  };

  useEffect(() => {
    const fetchChildStats = async () => {
      try {
        const response = await axios.get(
          `${process.env.REACT_APP_SERVER_IP}/statistics/personal`,
          {
            params: {
              childCode: childCode,
              userId: userId,
            },
          }
        );
        setChildStats(response.data);
        console.log("서버에서 받은 개인 통계 데이터:", response.data);
      } catch (error) {
        console.error("아동 통계 불러오기 실패:", error);
      }
    };

    if (childCode) {
      fetchChildStats(); //개인통계는 userId
    }
  }, [childCode, userId]);

  // array 0번은 일간, 1번은 주간, 2번은 월간
  const selectedStats =
      selected === "일간"
          ? childStats[0]
          : selected === "주간"
          ? childStats[1]
          : childStats[2];

  //추가:콘솔 로그
  useEffect(() => {
    console.log("선택한 기한:", selected);
    console.log("선택한 기한의 통계 data:", selectedStats);
  }, [childStats, selected]);

  return (
    <>
      <div className="stats-root">
        <div className="stats-top-div">
          {/*상단 제목*/}
          <h1>{childName ? `${childName} 아동의 통계` : "아동 통계"}</h1>
          <StatsDropdownContainer>
            <StatsDropdownToggle onClick={toggleDropdown}>
              {selected} <StatsCustomArrow $isopen={isopen} />
            </StatsDropdownToggle>
            {isopen && (
              <StatsDropdownMenu $isopen={isopen}>
                {options.map((option) => (
                  <StatsDropdownItem
                    key={option}
                    onClick={() => handleSelect(option)}
                  >
                    {option}
                  </StatsDropdownItem>
                ))}
              </StatsDropdownMenu>
            )}
          </StatsDropdownContainer>
        </div>
        {/*통계 내용 들어갈 곳*/}
        <div className="stats-main-div">
          <div className="stats-main-detail">
            <ScrollAnimation>
              <p className="stats-info-card">등록한 카드 종류의 비율</p>
              <PieChart
                imageCardNum={selectedStats.imageCardNum}
                defaultCardNum={selectedStats.defaultCardNum}
                videoCardNum={selectedStats.videoCardNum}
              />
            </ScrollAnimation>
            <ScrollAnimation>
              <div className="stats-bottom">
                <div className="stats-bottom-left">
                  <p className="stats-learning-info">가장 많이 학습한 카드</p>
                  {selectedStats.cardName &&
                  selectedStats.color &&
                  selectedStats.image ? (
                    <div className="stats-image-detail">
                      <StatsImagesDetail>
                        <ImgCard
                          key={`${selectedStats.cardName}-${selectedStats.image}`} //강제 렌더링 추가
                          imgName={selectedStats.cardName}
                          imgColor={`#${selectedStats.color}`}
                          imgSrc={selectedStats.image ? `${s3BaseUrl}${encodeURI(selectedStats.image)}` : NotFoundImg}
                        />
                      </StatsImagesDetail>
                    </div>
                  ) : (
                    <div className="stats-no-card">
                      <img src={NotFoundImg} alt="학습한 카드 없음" />
                    </div>
                  )}
                </div>
                <div className="stats-bottom-right">
                  <p className="stats-info">등록한 카드 수 </p>
                  <PersonalBarChart
                    imageCardNum={selectedStats.imageCardNum}
                    defaultCardNum={selectedStats.defaultCardNum}
                    videoCardNum={selectedStats.videoCardNum}
                  />
                </div>
              </div>
            </ScrollAnimation>
          </div>
        </div>
      </div>
    </>
  );
}

export default Stats;
