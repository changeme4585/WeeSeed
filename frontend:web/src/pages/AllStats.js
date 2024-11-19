import React, { useState, useCallback, useMemo } from "react";
import axios from "axios";
import { useLocation } from "react-router-dom";

import "../styles/AllStats.css";
import styled from "styled-components";
import upBtn from "../images/svg/up-arrow.svg";
import downBtn from "../images/svg/down-arrow.svg";
import ScrollAnimation from "../components/AnimationComponent/ScrollAnimation";

// 성별 차트 import
import GenderBarChart from "../components/chartComponent/GenderBarChart";
import GenderPieChart from "../components/chartComponent/GenderPieChart";

// 나이별 차트 import
import AgeBarChart from "../components/chartComponent/AgeBarChart";
import AgePieChart from "../components/chartComponent/AgePieChart";

//장애 등급별 차트 import
import GradeBarChart from "../components/chartComponent/GradeBarChart";
import GradePieChart from "../components/chartComponent/GradePieChart";

//장애 유형별 차트 import
import TypeBarChart from "../components/chartComponent/TypeBarChart";
import TypePieChart from "../components/chartComponent/TypePieChart";

//스타일 이걸로 고정해!!
const AllStatsDropdownContainer = styled.div`
  position: relative;
  display: flex;
  margin-top: 0px;
  width: 100%;
  justify-content: right;
`;

const AllStatsDropdownToggle = styled.button`
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
//큰 드롭메뉴
const AllStatsDropdownMenu = styled.ul`
  position: absolute;
  top: 100%;
  z-index: 1000;
  display: block;
  width: 300px;
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

const AllStatsCustomArrow = styled.span`
  width: 18px;
  height: 18px;
  margin-left: auto;
  background-size: cover;
  transition: transform 0.3s ease;
  background-image: url(${({ $isopen }) => ($isopen ? upBtn : downBtn)});
  transform: ${({ $isopen }) => ($isopen ? "rotate(180deg)" : "rotate(0)")};
`;

const AllStatsDropdownItem = styled.li`
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
  width: 290px;
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
//얘가 작은 dropdwon
const AllStatsGroupDropdownToggle = styled.button`
  width: 220px;
  height: 36px;
  padding: 5px 18px;
  border: solid 1px #ffce1b;
  cursor: pointer;
  background-color: var(--click-yellow);
  display: flex;
  justify-content: flex-end;
  align-items: center;
  font-family: "Pretendard-Regular";
  font-size: 12px;
`;

const AllStatsGroupDropdownMenu = styled.ul`
  position: absolute;
  top: 100%;
  z-index: 1000;
  display: block;
  width: 218px;
  padding: 0;
  margin: -2px 0 0;
  font-size: 12px;
  text-align: left;
  list-style: none;
  background-color: var(--white);
  border: solid 1px var(--dark-yellow);
  max-height: ${({ $isopen }) => ($isopen ? "500px" : "0")};
  overflow: hidden;
  opacity: ${({ $isopen }) => ($isopen ? "1" : "0")};
  transition: max-height 0.3s ease, opacity 0.3s ease;
`;

function AllStats() {
  const location = useLocation();
  const { userId } = location.state || {};
  const [allStats, setAllStats] = useState(null);
  const [isopen, setIsOpen] = useState(false);
  const [selected, setSelected] = useState("카테고리");

  const [ageGroup, setAgeGroup] = useState(null);
  const [isAgeDropdownOpen, setIsAgeDropdownOpen] = useState(false);
  const [ageData, setAgeData] = useState(null);

  const [genderGroup, setGenderGroup] = useState(null);
  const [isGenderDropdownOpen, setIsGenderDropdownOpen] = useState(false);
  const [genderData, setGenderData] = useState(null);

  const [gradeGroup, setGradeGroup] = useState(null);
  const [isGradeDropdownOpen, setIsGradeDropdownOpen] = useState(false);
  const [gradeData, setGradeData] = useState(null);

  const [typeGroup, setTypeGroup] = useState(null);
  const [isTypeDropdownOpen, setIsTypeDropdownOpen] = useState(false);
  const [typeData, setTypeData] = useState(null);

  const genderGroups = ["여아", "남아"];
  const ageGroups = ["0~5세", "5~9세", "10~14세", "15세 이상"];
  const gradeGroups = ["등급별 아동 수"];
  const typeGroups = ["유형별 아동 수"];

  const endpoints = useMemo(
    () => ({
      성별: "statistics/gender",
      나이별: "statistics/age",
      장애유형별: "statistics/disabilityType",
      장애등급별: "statistics/disabilityGrade",
    }),
    []
  );

  const fetchData = useCallback(
    async (endpoint) => {
      try {
        console.log(`${endpoint}로 요청 보냄. 유저 아이디 :`, userId); //서버에서 보내는게 이상해서 확인용
        const response = await axios.get(
          `${process.env.REACT_APP_SERVER_IP}/${endpoint}`
          //{ params: { userId } }
        );
        return response.data;
      } catch (error) {
        console.error(`${endpoint} 데이터 불러오기 실패:`, error);
        return null;
      }
    },
    [userId]
  );

  const fetchAgeData = useCallback(
    () => fetchData(endpoints.나이별),
    [fetchData, endpoints]
  );
  const fetchGenderData = useCallback(
    () => fetchData(endpoints.성별),
    [fetchData, endpoints]
  );
  const fetchGradeData = useCallback(
    () => fetchData(endpoints.장애등급별),
    [fetchData, endpoints]
  );
  const fetchTypeData = useCallback(
    () => fetchData(endpoints.장애유형별),
    [fetchData, endpoints]
  );

  const handleSelect = useCallback(
    async (option) => {
      setSelected(option);
      setIsOpen(false);

      if (option === "성별" && !genderData) {
        const data = await fetchGenderData();
        setGenderData(data);
        console.log("받아온 성별 데이터:", data);
        setIsGenderDropdownOpen(true);
      } else if (option === "나이별" && !ageData) {
        const data = await fetchAgeData();
        console.log("받아온 나이별 데이터:", data);
        setAgeData(data);
        setIsAgeDropdownOpen(true);
      } else if (option === "장애등급별" && !gradeData) {
        const data = await fetchGradeData();
        console.log("받아온 등급별 데이터:", data);
        setGradeData(data);
        setIsGradeDropdownOpen(true);
      } else if (option === "장애유형별" && !typeData) {
        const data = await fetchTypeData();
        console.log("받아온 유형별 데이터:", data);
        setTypeData(data);
        setIsTypeDropdownOpen(true);
      }
    },
    [
      ageData,
      genderData,
      gradeData,
      typeData,
      fetchAgeData,
      fetchGenderData,
      fetchGradeData,
      fetchTypeData,
    ]
  );

  const toggleDropdown = () => setIsOpen(!isopen);
  const toggleAgeDropdown = () => setIsAgeDropdownOpen(!isAgeDropdownOpen);
  const toggleGenderDropdown = () =>
    setIsGenderDropdownOpen(!isGenderDropdownOpen);
  const toggleGradeDropdown = () =>
    setIsGradeDropdownOpen(!isGradeDropdownOpen);
  const toggleTypeDropdown = () => setIsTypeDropdownOpen(!isTypeDropdownOpen);

  /*나이*/
  const handleAgeGroupSelect = (group) => {
    setAgeGroup(group);
    const filteredData = filterAgeDataByGroup(group, ageData);
    setAllStats(filteredData);
    setIsAgeDropdownOpen(false);
  };

  /*성별*/
  const handleGenderGroupSelect = (group) => {
    setGenderGroup(group);
    const filteredData = filterGenderDataByGroup(group, genderData);
    setAllStats(filteredData);
    setIsGenderDropdownOpen(false);
  };

  /*등급*/
  const handleGradeGroupSelect = (group) => {
    setGradeGroup(group);

    // gradeData가 올바르게 있는지 확인
    if (gradeData) {
      // 등급별 데이터를 추출하여 allStats로 설정
      const formattedData = [
        { id: "1급", label: "1급", value: gradeData.oneAac, color: "#F3B98B" },
        { id: "2급", label: "2급", value: gradeData.twoAac, color: "#F4E097" },
        {
          id: "3급",
          label: "3급",
          value: gradeData.threeAac,
          color: "#92D79E",
        },
        { id: "4급", label: "4급", value: gradeData.fourAac, color: "#A9CDE5" },
        { id: "5급", label: "5급", value: gradeData.fiveAac, color: "#B1C8FE" },
        { id: "6급", label: "6급", value: gradeData.sixAac, color: "#F4AFEF" },
      ];
      setAllStats(formattedData);
    }

    setIsGradeDropdownOpen(false);
  };

  /*장애유형*/
  const handleTypeGroupSelect = (group) => {
    setTypeGroup(group);

    if (typeData) {
      const formattedData = [
        {
          id: "자폐성 장애",
          category: "자폐성 장애",
          value: typeData.autism,
          color: "#F4E097",
        },
        {
          id: "지적 장애",
          category: "지적 장애",
          value: typeData.intellectual,
          color: "#92D79E",
        },
        {
          id: "행동 장애",
          category: "행동 장애",
          value: typeData.behavioral,
          color: "#A9CDE5",
        },
        {
          id: "발음 장애",
          category: "발음 장애",
          value: typeData.pronunciation,
          color: "#B1C8FE",
        },
        {
          id: "뇌병변 장애",
          category: "뇌병변 장애",
          value: typeData.brainLesion,
          color: "#F4AFEF",
        },
      ];
      setAllStats(formattedData);
    }
    setIsTypeDropdownOpen(false);
  };

  const filterAgeDataByGroup = (group, data) => {
    if (!data) return { aac: 0, video: 0 };
    if (group === "0~5세") {
      return { aac: data.zeroAac, video: data.zeroVideo };
    }
    if (group === "5~9세") {
      return { aac: data.fiveAac, video: data.fiveVideo };
    }
    if (group === "10~14세") {
      return { aac: data.tenAac, video: data.tenVideo };
    }
    if (group === "15세 이상") {
      return { aac: data.fifteenAac, video: data.fifteenVideo };
    }
    return { aac: 0, video: 0 };
  };

  const filterGenderDataByGroup = (group, data) => {
    if (!data) return { aac: 0, video: 0 };
    if (group === "남아") {
      return { aac: data.maleAac, video: data.maleVideo };
    }
    if (group === "여아") {
      return { aac: data.femaleAac, video: data.femaleVideo };
    }
    return null;
  };

  const renderChart = () => {
    if (selected === "성별" && genderGroup) {
      return (
        <div className="all-stats-main-detail">
          <ScrollAnimation>
            <p className="all-stats-info-card">등록한 카드의 비율</p>
            <GenderPieChart data={allStats} />
          </ScrollAnimation>
          <ScrollAnimation>
            <p className="all-stats-info">등록한 카드 수</p>
            <GenderBarChart data={allStats} />
          </ScrollAnimation>
        </div>
      );
    }

    if (selected === "나이별" && ageGroup) {
      return (
        <div className="all-stats-main-detail">
          <ScrollAnimation>
            <p className="all-stats-info-card">등록한 카드의 비율</p>
            <AgePieChart data={allStats} />
          </ScrollAnimation>
          <ScrollAnimation>
            <p className="all-stats-info">등록한 카드 수</p>
            <AgeBarChart data={allStats} />
          </ScrollAnimation>
        </div>
      );
    }

    if (selected === "장애등급별" && gradeGroup) {
      // 등급별 데이터를 추출하여 차트에 전달
      return (
        <div className="all-stats-main-detail">
          <ScrollAnimation>
            <p className="all-stats-info-card">장애 등급별 아동의 비율</p>
            <GradePieChart data={allStats} />
          </ScrollAnimation>
          <ScrollAnimation>
            <p className="all-stats-info">장애 등급별 아동 수</p>
            <GradeBarChart data={allStats} />
          </ScrollAnimation>
        </div>
      );
    }
    if (selected === "장애유형별" && typeGroup) {
      return (
        <div className="all-stats-main-detail">
          <ScrollAnimation>
            <p className="all-stats-info-card">장애 유형별 아동의 비율</p>
            <TypePieChart data={allStats} />
          </ScrollAnimation>
          <ScrollAnimation>
            <p className="all-stats-info">장애 유형별 아동 수</p>
            <TypeBarChart data={allStats} />
          </ScrollAnimation>
        </div>
      );
    }

    return null;
  };
  return (
    <div className="all-stats-root">
      <div className="all-stats-top-div">
        <h1>전체 아동 통계</h1>
        <AllStatsDropdownContainer>
          <AllStatsDropdownToggle onClick={toggleDropdown}>
            {selected} <AllStatsCustomArrow $isopen={isopen} />
          </AllStatsDropdownToggle>
          {isopen && (
            <AllStatsDropdownMenu $isopen={isopen}>
              {Object.keys(endpoints)
                .filter((option) => option !== "나이별") //나이별 제외 일단 ㅇㅇ..
                .map((option) => (
                  <AllStatsDropdownItem
                    key={option}
                    onClick={() => handleSelect(option)}
                  >
                    {option}
                  </AllStatsDropdownItem>
                ))}
            </AllStatsDropdownMenu>
          )}
        </AllStatsDropdownContainer>
      </div>
      {selected === "성별" && (
        <AllStatsDropdownContainer>
          <AllStatsGroupDropdownToggle onClick={toggleGenderDropdown}>
            {genderGroup || "성별 선택"}{" "}
            <AllStatsCustomArrow $isopen={isGenderDropdownOpen} />
          </AllStatsGroupDropdownToggle>
          {isGenderDropdownOpen && (
            <AllStatsGroupDropdownMenu $isopen={isGenderDropdownOpen}>
              {genderGroups.map((group) => (
                <AllStatsDropdownItem
                  key={group}
                  onClick={() => handleGenderGroupSelect(group)}
                >
                  {group}
                </AllStatsDropdownItem>
              ))}
            </AllStatsGroupDropdownMenu>
          )}
        </AllStatsDropdownContainer>
      )}
      {selected === "나이별" && (
        <AllStatsDropdownContainer>
          <AllStatsGroupDropdownToggle onClick={toggleAgeDropdown}>
            {ageGroup || "연령대 선택"}{" "}
            <AllStatsCustomArrow $isopen={isAgeDropdownOpen} />
          </AllStatsGroupDropdownToggle>
          {isAgeDropdownOpen && (
            <AllStatsGroupDropdownMenu $isopen={isAgeDropdownOpen}>
              {ageGroups.map((group) => (
                <AllStatsDropdownItem
                  key={group}
                  onClick={() => handleAgeGroupSelect(group)}
                >
                  {group}
                </AllStatsDropdownItem>
              ))}
            </AllStatsGroupDropdownMenu>
          )}
        </AllStatsDropdownContainer>
      )}
      {selected === "장애등급별" && (
        <AllStatsDropdownContainer>
          <AllStatsGroupDropdownToggle onClick={toggleGradeDropdown}>
            {gradeGroup || "등급 선택"}{" "}
            <AllStatsCustomArrow $isopen={isGradeDropdownOpen} />
          </AllStatsGroupDropdownToggle>
          {isGradeDropdownOpen && (
            <AllStatsGroupDropdownMenu $isopen={isGradeDropdownOpen}>
              {gradeGroups.map((group) => (
                <AllStatsDropdownItem
                  key={group}
                  onClick={() => handleGradeGroupSelect(group)}
                >
                  {group}
                </AllStatsDropdownItem>
              ))}
            </AllStatsGroupDropdownMenu>
          )}
        </AllStatsDropdownContainer>
      )}
      {selected === "장애유형별" && (
        <AllStatsDropdownContainer>
          <AllStatsGroupDropdownToggle onClick={toggleTypeDropdown}>
            {typeGroup || "유형 선택"}{" "}
            <AllStatsCustomArrow $isopen={isTypeDropdownOpen} />
          </AllStatsGroupDropdownToggle>
          {isTypeDropdownOpen && (
            <AllStatsGroupDropdownMenu $isopen={isTypeDropdownOpen}>
              {typeGroups.map((group) => (
                <AllStatsDropdownItem
                  key={group}
                  onClick={() => handleTypeGroupSelect(group)}
                >
                  {group}
                </AllStatsDropdownItem>
              ))}
            </AllStatsGroupDropdownMenu>
          )}
        </AllStatsDropdownContainer>
      )}
      <div className="all-stats-main-div">{renderChart()}</div>
    </div>
  );
}

export default AllStats;
