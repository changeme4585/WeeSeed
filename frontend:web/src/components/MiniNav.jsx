import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import logoImg from "../images/MiniNav-Logo.svg"; //미니 네비게이션 위에 로고
import image from "../images/svg/image-icon.svg"; // aac 화면
import videoImage from "../images/svg/video-icon.svg";
import growthImage from "../images/svg/diary-icon.svg";
import statsImage from "../images/svg/stat-icon.svg";
import settingImage from "../images/svg/settings-icon.svg";
import logoutImage from "../images/svg/logout-icon.svg";
import LogoutAlert from "./userComponent/LogoutAlert"; //로그아웃 알림창

function MiniNav({ setActiveMenu, setIsMiniNav, setIsStatNav }) {
  const navigate = useNavigate();
  const [showLogoutModal, setShowLogoutModal] = useState(false); //로그아웃 모달

  const handleLogoutClick = () => {
    setShowLogoutModal(true); //로그아웃 모달창 띄우기
  };

  const handleNavigate = (path) => {
    setActiveMenu(path);
    setIsMiniNav(false);
    setIsStatNav(false);
    navigate(path);
  };

  const handleStatsClick = () => {
    setIsMiniNav(false);
    setIsStatNav(false);
  };

  const styles = {
    sidebar: {
      position: "fixed",
      top: "0px",
      left: "0px",
      height: "100vh",
      width: "116px",
      backgroundColor: "#FFCE1B",
      display: "flex",
      flexDirection: "column",
      justifyContent: "space-between",
      padding: "20px 0",
      boxShadow: "4px 0px 4px rgba(0, 0, 0, 0.25)",
    },
    navTop: {
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
    },
    navBottom: {
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      marginBottom: "50px",
    },
    menu: {
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      padding: "10px 0",
      cursor: "pointer",
    },
    mininavImg: {
      width: "56.57px",
      height: "57px",
      filter:
        "brightness(0) saturate(100%) invert(51%) sepia(75%) saturate(1281%) hue-rotate(5deg) brightness(94%) contrast(101%)", //벡터 이미지에 색상
    },
    statImg: {
      width: "56.57px",
      height: "57px",
    },
    logoImg: {
      width: "90px",
      height: "61.82px",
      marginBottom: "49px",
    },
    logoutBtn: {
      background: "none",
      border: "none",
      cursor: "pointer",
    },
  };

  return (
    <div style={styles.sidebar}>
      <div style={styles.navTop}>
        <div style={styles.menu}>
          <img src={logoImg} alt="로고" style={styles.logoImg} />
        </div>
        <div style={styles.menu} onClick={() => handleNavigate("/aac")}>
          <img src={image} alt="사진" style={styles.mininavImg} />
        </div>
        <div style={styles.menu} onClick={() => handleNavigate("/video")}>
          <img src={videoImage} alt="영상" style={styles.mininavImg} />
        </div>
        <div
          style={styles.menu}
          onClick={() => handleNavigate("/growth-diary")}
        >
          <img src={growthImage} alt="성장일지" style={styles.mininavImg} />
        </div>
        <div style={styles.menu} onClick={handleStatsClick}>
          <img src={statsImage} alt="통계" style={styles.statImg} />
        </div>
      </div>
      <div style={styles.navBottom}>
        <div style={styles.menu} onClick={() => handleNavigate("/settings")}>
          <img src={settingImage} alt="설정" style={styles.statImg} />
        </div>
        <button
          style={{ ...styles.menu, ...styles.logoutBtn }}
          onClick={handleLogoutClick}
        >
          <img src={logoutImage} alt="로그아웃" style={styles.statImg} />
        </button>
      </div>

      {showLogoutModal && (
        <LogoutAlert
          isModalOpen={showLogoutModal}
          closeModal={() => setShowLogoutModal(false)}
        />
      )}
    </div>
  );
}

export default MiniNav;
