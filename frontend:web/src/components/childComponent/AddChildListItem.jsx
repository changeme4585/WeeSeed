import React, { useState } from "react";
import saveChildIcon from "../../images/svg/check-icon.svg";
import moreInfoIcon from "../../images/svg/down-arrow.svg";
import axios from "axios";

const AddChildListItem = ({ onChildAdded }) => {
  const [childData, setChildData] = useState({
    name: "",
    gender: "남자", // Default gender
    birthYear: "2010", // Default birth year
    birthMonth: "01",
    birthDay: "01",
    grade: "",
    disabilityType: "",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setChildData({
      ...childData,
      [name]: value,
    });
  };

  const handleSave = async () => {
    if (!validateInput()) {
      alert("모든 정보를 입력해주세요!");
      return;
    }

    // Prepare data for server
    const userId = localStorage.getItem("userId");
    const birth = `${childData.birthYear}년 ${childData.birthMonth}월 ${childData.birthDay}일`;
    const grade = parseInt(childData.grade.replace("급", ""), 10);
    const childDataToSend = {
      userId,
      name: childData.name,
      gender: childData.gender,
      birth,
      grade,
      disabilityType: childData.disabilityType,
    };

    try {
      const response = await axios.post(
          `${process.env.REACT_APP_SERVER_IP}/child/register`,
          childDataToSend,
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
      );
      if (response.status === 200) {
        onChildAdded(childData); // Notify parent component
        alert("아동 추가 성공!");
      }
    } catch (error) {
      console.error("아동 추가 오류:", error);
    }
  };

  const validateInput = () => {
    return (
        childData.name &&
        childData.grade &&
        childData.grade !== "장애 등급" &&
        childData.disabilityType &&
        childData.disabilityType !== "장애 유형"
    );
  };

  const [isExpanded, setIsExpanded] = useState(false);
  const toggleExpand = () => setIsExpanded(!isExpanded);

  const handleSelectClick = (e) => {
    e.stopPropagation(); // Prevent event bubbling
  };

    const styles = `
    .total-child-div {
      width: 100%;
    }
    .child-list-item {
      background-color: var(--light-yellow);
      padding: 0px;
      width: 100%;
      box-sizing: border-box;
      overflow: hidden;
      transition: max-height 0.3s ease;
      display: block;
      border-radius: 5px;
      margin-bottom: 20px;
    }
    .child-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      cursor: pointer;
      width: 820px;
      height: 60px;
      box-sizing: border-box;
      margin-bottom: 0px;
    }
    .child-name-input {
      font-family: "Pretendard-Bold";
      font-size: 16px;
      margin-right: auto;
      width: 220px; 
      height: 36px; 
      border: 2px solid rgba(255, 206, 27, 1);
      background-color: rgba(255, 253, 234, 1);
      outline: none;
      box-shadow: none;
    }
    .child-info {
      display: flex;
      align-items: flex-end;
      margin-right: 65px;
    }
    .child-details {
      display: flex;
      margin-top: 0px;
      max-height: 200px;
      overflow: hidden;
    }
    .add-detail-item {
      display: flex;
      align-items: center;
      height: 60px;
      background-color: rgba(247, 246, 245, 1);
      padding: 5px 10px;
    }
    .add-detail-item span {
      font-family: "Pretendard-Regular";
      font-size: 16px;
      margin: 0 40px;
    }
    .add-detail-item select {
      width: 225px; 
      height: 36px;
      font-family: "Pretendard-Regular";
      border: 1px solid rgba(255, 206, 27, 1); 
      background-color: rgba(255, 253, 234, 1);
      font-size: 16px;
    }
    
    .add-detail-item .grade-item {
      margin-left: 50px;
      margin-right: 255px;
    }
    .child-action-buttons {
      display: flex;
      justify-content: flex-end;
      margin-top: 10px;
    }
    .save-icon {
      width: 22px;
      height: 22px;
      cursor: pointer;
      margin-left: 10px;
      margin-right:10px;
    }
    .more-child-info-btn {
      margin-right: 10px;
      cursor: pointer;
      width: 20px;
      height: 20px;
      transform: rotate(${isExpanded ? "180deg" : "0deg"});
      transition: transform 0.3s ease;
    }
    .radio-group {
      display: flex;
      align-items: center;
      padding: 0px;
      margin-right: 83px;
    }
    label {
      display: flex;
      align-items: center;
      cursor: pointer;
      margin-right: 14px;
      font-family: "Pretendard-Regular";
      font-size: 16px;
    }
    input[type="radio"] {
      display: none; 
    }
    label::before {
      content: "";
      display: inline-block;
      width: 20px;
      height: 20px;
      border: 2px solid rgba(255, 206, 27, 1);
      border-radius: 50%;
      margin-right: 8px;
      background-color: white; /* 기본 상태 배경색 */
      transition: background-color 0.2s, box-shadow 0.2s;
    }
    input[type="radio"]:checked + label::before {
      background-color: white; 
      box-shadow: inset 0 0 0 5px rgba(255, 206, 27, 1);
    }
    input[type="radio"]:hover + label::before {
      border-color: rgba(255, 206, 27, 1);
  `;
    return (
        <div className={`child-list-item ${isExpanded ? "expanded" : ""}`}>
            <style>{styles}</style>
            <div className="child-header" onClick={toggleExpand}>
                <img
                    src={moreInfoIcon}
                    alt="MoreInfo"
                    className="more-child-info-btn"
                />
                <input
                    type="text"
                    name="name"
                    value={childData.name}
                    onChange={handleInputChange}
                    placeholder="   이름"
                    className="child-name-input"
                />

                <div className="radio-group">
                    <input
                        type="radio"
                        id="male"
                        name="gender"
                        value="남자"
                        checked={childData.gender === "남자"}
                        onChange={handleInputChange}
                    />
                    <label htmlFor="male">남자</label>
                    <input
                        type="radio"
                        id="female"
                        name="gender"
                        value="여자"
                        checked={childData.gender === "여자"}
                        onChange={handleInputChange}
                    />
                    <label htmlFor="female">여자</label>
                </div>
                <div className="child-info">
                    <select
                        name="birthYear"
                        value={childData.birthYear}
                        onChange={handleInputChange}
                        onClick={handleSelectClick}
                        className="birth-year-select"
                    >
                        {Array.from({ length: 121 }, (_, i) => 1900 + i).map((year) => (
                            <option key={year} value={year}>
                                {year}년
                            </option>
                        ))}
                    </select>

                    <select
                        name="birthMonth"
                        value={childData.birthMonth}
                        onChange={handleInputChange}
                        onClick={handleSelectClick}
                        className="birth-month-select"
                    >
                        {Array.from({ length: 12 }, (_, i) => i + 1).map((month) => (
                            <option key={month} value={String(month).padStart(2, "0")}>
                                {month}월
                            </option>
                        ))}
                    </select>

                    <select
                        name="birthDay"
                        value={childData.birthDay}
                        onChange={handleInputChange}
                        onClick={handleSelectClick}
                        className="birth-day-select"
                    >
                        {Array.from({ length: 31 }, (_, i) => i + 1).map((day) => (
                            <option key={day} value={String(day).padStart(2, "0")}>
                                {day}일
                            </option>
                        ))}
                    </select>
                </div>
                <img
                    src={saveChildIcon}
                    alt="저장"
                    onClick={handleSave}
                    className="save-icon"
                />
            </div>

            <div className="child-details">
                <div className="add-detail-item">
                    <select
                        name="grade"
                        value={childData.grade}
                        onChange={handleInputChange}
                        className="grade-item"
                    >
                        <option value="장애 등급">장애 등급</option>
                        <option value="1급">1급</option>
                        <option value="2급">2급</option>
                        <option value="3급">3급</option>
                        <option value="4급">4급</option>
                        <option value="5급">5급</option>
                        <option value="6급">6급</option>
                    </select>
                    <select
                        name="disabilityType"
                        value={childData.disabilityType}
                        onChange={handleInputChange}
                    >
                        <option value="장애 유형">장애 유형</option>
                        <option value="자폐성장애">자폐성 장애</option>
                        <option value="지적장애">지적 장애</option>
                        <option value="행동장애">행동 장애</option>
                        <option value="발음장애">발음 장애</option>
                        <option value="뇌병변장애">뇌병변 장애</option>
                    </select>
                </div>
            </div>
        </div>
    );
};

export default AddChildListItem;
