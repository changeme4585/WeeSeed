/*통계 버튼 누르면 뜨는 아동 목록*/
import React, { useState, useEffect, useContext } from "react";
import SelectIcon from "../images/svg/right-arrow-stats.svg"; //선택 버튼
import { ChildContext } from "../index";
import { useNavigate } from "react-router-dom"; //네비게이트..
import childProfileIcon from "../images/svg/stat-child-profile.svg"; //아동 프로필 사진

const styles = {
  statNav: {
    position: "fixed",
    top: "0",
    left: "116px",
    height: "100vh",
    width: "370px",
    backgroundColor: "#D9D4CF",
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
  },
  statNavHeader: {
    display: "flex",
    alignItems: "center",
    fontFamily: "Pretendard-Bold",
    fontSize: "32px",
    marginLeft: "16px",
    marginTop: "40px",
  },
  statNavContent: {
    flexGrow: "1",
    marginTop: "0px",
    marginLeft: "16px",
    maxHeight: "80%",
    overflowY: "auto",
  },
  statNavItem: {
    fontFamily: "Pretendard-Regular",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    padding: "20px 0px",
    paddingLeft: "6px",
  },
  statNavItemText: {
    flexGrow: "1",
    cursor: "pointer",
    fontSize: "16px",
    marginTop: "3px",
  },
  statNavItemDate: {
    marginLeft: "0px",
    fontSize: "13px",
  },
  statNavItemArrow: {
    marginRight: "16px",
    width: "36px",
    height: "36px",
    cursor: "pointer",
  },
  statNavItemImage: {
    width: "100%",
    height: "100%",
  },

  statNavButton: {
    fontFamily: "Pretendard-Bold",
    fontSize: "16px",
    alignSelf: "center",
    backgroundColor: "rgba(255, 206, 27, 1)",
    color: "rgba(17, 17, 17, 1)",
    border: "none",
    cursor: "pointer",
    marginBottom: "28px",
    width: "310px",
    height: "40px",
    transition: "background-color 0.3s ease",
  },
  statNavButtonHover: {
    backgroundColor: "rgba(255, 173, 0, 1)",
    color: "rgba(17, 17, 17, 1)",
    fontFamily: "Pretendard-Bold",
  },
  childInfoDiv: {
    display: "flex",
    flexDirection: "column",
    backgroundColor: "var(--light--yellow)",
    height: "42px",
    width: "245px",
    marginLeft: "10px",
  },
  childProfile: {
    width: "48px",
    height: "48px",
    marginRight: "10px",
  },
};

function StatNav({ setActiveMenu, onSelectChild, setIsMiniNav, setIsStatNav }) {
  const [childList, setChildList] = useState([]);
  const { setSelectedChild } = useContext(ChildContext);
  const navigate = useNavigate();
  const [isHovered, setIsHovered] = useState(false);
  const userType = localStorage.getItem("userType"); //보호자는 개인만 볼수있게

  //로컬에서 아동 정보 불러오기
  useEffect(() => {
    const storedChild = JSON.parse(localStorage.getItem("child"));
    if (storedChild) {
      setChildList(storedChild);
    }
  }, []);

  //여기서 get 요청하지 말고 Child Code랑 userId를 Stats 페이지에 전달하는게 맞을듯?
  const handleChoiceChild = async (child) => {
    setSelectedChild(child); // 선택된 아동
    const userId = localStorage.getItem("userId"); //유저 아이디
    const childCode = child.childCode; //아동 코드
    setActiveMenu("stats");
    // StatNav와 MiniNav 상태를 false로 변경해서 Nav로 돌아가게 함
    setIsMiniNav(false);
    setIsStatNav(false);

    navigate(
      "/stats",
      { state: { userId, childCode, childName: child.name } },
      { replace: true }
    );
  };
  //전체보기에선 userId만
  const handleViewAll = () => {
    setSelectedChild(null);
    const userId = localStorage.getItem("userId");
    setActiveMenu("stats");
    // StatNav와 MiniNav 상태를 false로 변경해서 Nav로 돌아가게 함
    setIsMiniNav(false);
    setIsStatNav(false);
    // navigate 호출 이전에 상태를 false로 설정하여 Nav로 전환
    setTimeout(() => {
      navigate("/all-stats", { state: { userId } });
    }, 0); // 상태가 업데이트된 후에 navigate를 호출하기 위해 setTimeout 사용
  };

  return (
    <div style={styles.statNav}>
      <div style={styles.statNavHeader}>
        <h2>통계</h2>
      </div>
      <div style={styles.statNavContent}>
        {childList.map((child, index) => (
          <div
            key={index}
            style={styles.statNavItem}
            onClick={() => handleChoiceChild(child)}
          >
            <span style={styles.childProfile}>
              <img src={childProfileIcon} alt="child profile" />
            </span>
            <div style={styles.childInfoDiv}>
              <span style={styles.statNavItemText}>{child.name}</span>
              <span style={styles.statNavItemDate}>{child.birth}</span>
            </div>

            <span style={styles.statNavItemArrow}>
              <img
                src={SelectIcon}
                alt="Select Icon"
                style={styles.statNavItemImage}
              />
            </span>
          </div>
        ))}
      </div>
      <button
        style={{
          ...styles.statNavButton,
          ...(isHovered && styles.statNavButtonHover),
          visibility: userType === "Path" ? "visible" : "hidden",
        }}
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
        onClick={handleViewAll}
      >
        전체 보기
      </button>
    </div>
  );
}

export default StatNav;
