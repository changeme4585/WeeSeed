/*보호자 버전 사이드바*/
import React from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import "../styles/Nav.css";
import image from "../images/svg/사진.svg";
import videoImage from "../images/svg/영상.svg";
import growthImage from "../images/svg/성장일지.svg";
import notifyImage from "../images/svg/알림.svg";
import settingImage from "../images/svg/설정.svg";
import logoutImage from "../images/svg/로그아웃.svg";

function NavN() {
  /*로그아웃 버튼 처리*/
  const handleLogout = async () => {
    try {
      const response = await fetch("http://192.168.117.149:8080/logout", {
        method: "POST",
      });
      const data = await response.text(); // 응답 본문을 텍스트로 변환
      if (response.ok) {
        localStorage.removeItem("token");
        navigate("/"); // 로그아웃 후 로그인 페이지로 이동
      } else {
        console.error("로그아웃 실패:", data); // 실패 시 에러 메시지 출력
      }
    } catch (error) {
      console.error("로그아웃 에러:", error);
    }
  };

  const navigate = useNavigate();

  return (
    <div className="nav-bar">
      <div className="nav-bar__top">
        <Link className="menu" to={"/aac"}>
          <img src={image} alt="사진" className="nav-img" />
          말해봐요
        </Link>
        <Link className="menu" to={"/video"}>
          <img src={videoImage} alt="사진" className="nav-img" />
          따라해요
        </Link>
        <Link className="menu" to={"/growth-diary"}>
          <img src={growthImage} alt="사진" className="nav-img" />
          성장일지
        </Link>
        <button className="menu">
          {/* 알림용 사이드바 필요 */}
          <img src={notifyImage} alt="사진" className="nav-img" />
          알림
        </button>
      </div>
      <div className="nav-bar__bottom">
        <Link className="menu" to={"/settings"}>
          <img src={settingImage} alt="사진" className="nav-img" />
          설정
        </Link>
        <button className="menu logout-btn" onClick={handleLogout}>
          <img src={logoutImage} alt="사진" className="nav-img" />
          로그아웃
        </button>
      </div>
    </div>
  );
}

export default NavN;
