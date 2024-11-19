//탈퇴, 프로필 수정시 입력해야 하는 비밀번호 입력창

import React, { useState } from "react";
import styled from "styled-components";
import xButton from "../../images/svg/close-icon.svg";
import coverPW from "../../images/svg/감은 눈.svg"; //비밀번호 숨김 아이콘
import openPW from "../../images/svg/눈.svg"; //비번 표시

const CheckPWModalBg = styled.div`
  display: flex;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9999;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  background-color: transparent;
`;

const CheckPWModalBox = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 440px;
  height: 220px;
  background-color: var(--light-pink);
  border-radius: 15px;
  position: relative;
  box-shadow: 1px 1px 4px var(--gray);

  h2 {
    font-family: "Pretendard-Bold";
  }
  p {
    font-family: "Pretendard-Regular";
    font-size: 12px;
  }
  div {
    display: flex;
    justify-content: space-between;
    width: 290px;
    margin-top: 0px;
  }
`;

const CheckPWButton = styled.button`
  background-color: var(--yellow);
  border: 1px solid var(--yellow);
  color: var(--black);
  cursor: pointer;
  font-size: 12px;
  border: none;
  width: 285px;
  height: 30px;
  margin-left: 0px;
  margin-bottom: 25px;

  &:hover {
    background-color: var(--black);
    color: var(--pink);
  }
  font-family: "Pretendard-Bold";
`;

const CheckPWCloseButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
`;

const CheckPWInput = styled.div`
  display: flex;
  flex-direction: column;

  input {
    /* input 스타일 */
    width: 280px;
    height: 30px;
    border: 1px solid var(--dark-pink);
    font-size: 12px;
    font-family: "Pretendard-Regular";
    box-shadow: none;
  }
  //눈 모양 위치 조정 필요;;; 안보여서 임시로 해둠
  .eye-icon {
    position: absolute;
    right: 5px;
    top: 5px;
    width: 20px;
    cursor: pointer;
  }
`;

const CheckPW = ({ closeModal }) => {
  const [showPassword, setShowPassword] = useState(false); //비번 표시 상태
  const [password, setPassword] = useState(""); // 비밀번호 상태 관리

  const handleChange = (e) => {
    setPassword(e.target.value);
  };

  return (
    <CheckPWModalBg>
      <CheckPWModalBox>
        <CheckPWCloseButton onClick={closeModal}>
          <img src={xButton} alt="Close" />
        </CheckPWCloseButton>
        <h2>비밀번호 입력</h2>
        <p>본인 인증을 위해 비밀번호를 입력해주세요</p>
        <CheckPWInput>
          <input
            required
            placeholder="비밀번호를 입력하세요"
            type={showPassword ? "text" : "password"} //눈 아이콘에 따라 test-pw 타입 다름
            value={password}
            onChange={handleChange}
          />
          <img
            src={showPassword ? openPW : coverPW} //눈 아이콘 전환
            alt="Show/Hide Password"
            className="eye-icon"
            onClick={() => setShowPassword(!showPassword)}
          />
          <CheckPWButton>확인</CheckPWButton>
        </CheckPWInput>
      </CheckPWModalBox>
    </CheckPWModalBg>
  );
};

export default CheckPW;
