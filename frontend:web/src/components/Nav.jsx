/* 재활사 버전 사이드바 */
import React, { useState, useEffect } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import MiniNav from "./MiniNav";
import StatNav from "./StatNav"; // 통계 아동 목록 네비게이션바
import "../styles/Nav.css";
import NavLogo from "../images/Nav-Logo.svg"; //네이게이션 로고
import image from "../images/svg/image-icon.svg";
import videoImage from "../images/svg/video-icon.svg";
import growthImage from "../images/svg/diary-icon.svg";
import statsImage from "../images/svg/stat-icon.svg";
import settingImage from "../images/svg/settings-icon.svg";
import logoutImage from "../images/svg/logout-icon.svg";
import LogoutAlert from "./userComponent/LogoutAlert"; //로그아웃 알림창

function Nav() {
  const navigate = useNavigate();
  const location = useLocation();
  const [isMiniNav, setIsMiniNav] = useState(false);
  const [isStatNav, setIsStatNav] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); //로그아웃 모달
  //const userType = localStorage.getItem("userType"); // 알림 or 통계 버튼을 위한거
  const [activeMenu, setActiveMenu] = useState(""); //버튼 선택시 box shadow 주기 위해..

  useEffect(() => {
    if (location.pathname !== "/stats" && location.pathname !== "/all-stats") {
      setIsMiniNav(false);
      setIsStatNav(false);
    }
  }, [location.pathname]);

  const handleLogoutClick = () => {
    setShowLogoutModal(true); //로그아웃 모달창 띄우기
  };

  const handleStatsClick = () => {
    setActiveMenu("stats"); //통계 버튼 눌렀을 경우 box-shadow 없애기
    if (isMiniNav && isStatNav) {
      setIsMiniNav(false);
      setIsStatNav(false);
    } else {
      setIsMiniNav(true);
      setIsStatNav(true);
    }
  };

  const handleNavItemClick = (path) => {
    setActiveMenu(path);
    setIsMiniNav(false);
    setIsStatNav(false);
    navigate(path);
  };

  return (
    <div className="nav-wrapper">
      <div className={`nav-bar ${isMiniNav ? "nav-bar-hidden" : ""}`}>
        <div className="nav-bar__top">
          <img src={NavLogo} className="nav-logo" alt="로고" />
          <Link
            className={`menu ${activeMenu === "/aac" ? "active" : ""}`}
            to="/aac"
            onClick={() => handleNavItemClick("/aac")}
          >
            <img src={image} alt="aac" className="nav-img" />
            <span className="menu-text">말해봐요</span>
          </Link>
          <Link
            className={`menu ${activeMenu === "/video" ? "active" : ""}`}
            to="/video"
            onClick={() => handleNavItemClick("/video")}
          >
            <img src={videoImage} alt="비디오" className="nav-img" />
            <span className="menu-text">따라해요</span>
          </Link>
          <Link
            className={`menu ${activeMenu === "/growth-diary" ? "active" : ""}`}
            to="/growth-diary"
            onClick={() => handleNavItemClick("/growth-diary")}
          >
            <img src={growthImage} alt="성장일지" className="nav-img" />
            <span className="menu-text">성장일지</span>
          </Link>
          <div
            className={`menu ${activeMenu === "stats" ? "active" : ""}`}
            onClick={handleStatsClick}
          >
            <img src={statsImage} alt="통계" className="nav-img" />
            <span className="menu-text">통계</span>
          </div>
        </div>
        <div className="nav-bar__middle"> </div>
        <div className="nav-bar__bottom">
          <Link
            className={`menu ${activeMenu === "/settings" ? "active" : ""}`}
            to="/settings"
            onClick={() => handleNavItemClick("/settings")}
          >
            <img src={settingImage} alt="설정" className="nav-img" />
            <span className="menu-text">설정</span>
          </Link>
          <button className="menu logout-btn" onClick={handleLogoutClick}>
            <img src={logoutImage} alt="로그아웃" className="nav-img" />
            <span className="menu-text">로그아웃</span>
          </button>
        </div>
      </div>
      <div
        className={`mini-nav-wrapper ${isMiniNav ? "mini-nav-visible" : ""}`}
      >
        {isMiniNav && (
          <MiniNav
            setActiveMenu={setActiveMenu}
            setIsMiniNav={setIsMiniNav}
            setIsStatNav={setIsStatNav}
          />
        )}
      </div>
      <div
        className={`stat-nav-wrapper ${isStatNav ? "stat-nav-visible" : ""}`}
      >
        {isStatNav && (
          <StatNav
            setActiveMenu={setActiveMenu}
            setIsMiniNav={setIsMiniNav}
            setIsStatNav={setIsStatNav}
          />
        )}
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

export default Nav;
