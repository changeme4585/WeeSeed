/*탈퇴 확인 모달창*/
import React, { useState } from "react";
import styled from "styled-components";
import axios from "axios";
import { useNavigate } from "react-router-dom"; //탈퇴 후 페이지 이동
import xButton from "../../images/svg/close-icon.svg";

const ResignConfirmBg = styled.div`
  display: flex;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.6);
`;

const ResignConfirmBox = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-evenly;
  width: 480px;
  height: 242px;
  background-color: #f7f6f5;
  border-radius: 15px;
  position: relative;
  box-shadow: 1px 3px 5px rgba(0, 0, 0, 0.2);

  h1 {
    font-size: 32px;
    font-family: "Pretendard-Bold";
    margin-top: 40px;
  }
  p {
    font-family: "Pretendard-Regular";
    margin-top: 25px;
    margin-bottom: 40px;
    font-size: 16px;
  }
  div {
    display: flex;
    justify-content: space-between;
    width: 300px;
    height: 36px;
    margin-bottom: 40px;
  }
`;

const ResignConfirmButton = styled.button`
  background-color: rgba(255, 206, 27, 1);
  color: rgba(17, 17, 17, 1);
  cursor: pointer;
  font-size: 16px;
  border: none;
  width: 144px;
  height: 36px;
  font-family: "Pretendard-Regular";
  transition: background-color 0.3s ease;
  &:hover {
    background-color: rgba(255, 173, 0, 1);
    color: rgba(17, 17, 17, 1);
    font-family: "Pretendard-Regular";
  }
`;

const ResignConfirmButtonC = styled.button`
  background-color: #d9d4cf;
  color: #111111;
  cursor: pointer;
  font-size: 16px;
  border: none;
  width: 144px;
  height: 36px;
  font-family: "Pretendard-Regular";
  transition: background-color 0.3s ease;
  &:hover {
    background-color: #b6aea5;
    color: #111111;
    font-family: "Pretendard-Regular";
  }
`;

const ResignConfirmCloseButton = styled.button`
  position: absolute;
  top: 20px;
  right: 20px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
`;

const ResignConfirm = ({ closeModal }) => {
  const navigate = useNavigate();

  const handleCloseButtonClick = () => {
    closeModal();
  };

  //버튼 클릭 처리
  const handleConfirmClick = async () => {
    try {
      const userId = localStorage.getItem("userId");
      const userType = localStorage.getItem("userType");

      const response = await axios.post(
        `${process.env.REACT_APP_SERVER_IP}/user/resign`,
        null,
        {
          params: {
            constructorId: userId,
            state: userType,
          },
        }
      );

      if (response.status === 200) {
        alert("탈퇴 성공");
        localStorage.clear(); // 로컬스토리지 초기화
        navigate("/"); // 홈 화면으로 이동
      } else {
        alert("탈퇴 실패");
      }
    } catch (error) {
      console.error("탈퇴 처리 중 오류 발생:", error);
      alert("서버 오류로 탈퇴에 실패했습니다.");
    } finally {
      handleCloseButtonClick(); // 모달 닫기
    }
  };

  return (
    <ResignConfirmBg>
      <ResignConfirmBox>
        <ResignConfirmCloseButton onClick={handleCloseButtonClick}>
          <img src={xButton} alt="Close" />
        </ResignConfirmCloseButton>
        <h1>탈퇴하시겠습니까?</h1>
        <p>탈퇴 후 weeseed 서비스를 이용할 수 없습니다</p>
        <div>
          <ResignConfirmButtonC onClick={handleCloseButtonClick}>
            취소
          </ResignConfirmButtonC>
          <ResignConfirmButton onClick={handleConfirmClick}>
            확인
          </ResignConfirmButton>
        </div>
      </ResignConfirmBox>
    </ResignConfirmBg>
  );
};

export default ResignConfirm;
