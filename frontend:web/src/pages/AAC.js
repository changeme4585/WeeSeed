import React, {
  useState,
  useEffect,
  useCallback,
  useContext,
  useRef,
} from "react";
import "../styles/AAC.css";
import axios from "axios";
import SearchImg from "../images/svg/search-icon.svg";
import AddCardImg from "../images/svg/aac-btn.svg";
import PrintImg from "../images/svg/print-icon.svg";
import MultiPrintImg from "../images/svg/check-icon.svg"; //다중 프린트 아이콘
import DeleteAAC from "../images/svg/delete-icon.svg";
import SelectColor from "../components/SelectColor";
import AACList from "../components/aacComponent/AACList";
import CreateAAC from "../components/aacComponent/CreateAAC";
import ViewAAC from "../components/aacComponent/ViewAAC";
import { ChildContext } from "../index";
import ReactToPrint from "react-to-print"; // 다중 프린트용
import { translateCardName } from "../components/aacComponent/ImgCard"; // 디폴트 카드 이름 한글로 바꾸기
import styled from "styled-components";

const MultiPrintContainer = styled.div`
  display: none;
  @media print {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
    gap: 20px;
    padding: 20px;
    width: 100%;
    grid-template-columns: repeat(3, 1fr);
    page-break-inside: avoid;
  }
`;

const MultiPrintBox = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 15px;
  background-color: ${({ bgColor }) => bgColor || "#767676"};
  width: 220px;
  height: 264px;
  text-align: center;
  overflow: hidden;
`;
const MultiPrintCardImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const MultiPrintTitle = styled.h2`
  font-family: "Pretendard-Regular";
  font-size: 15px;
  color: white;
`;

function AAC() {
  const printRef = useRef();
  const printTriggerRef = useRef();

  const [selectable, setSelectable] = useState(false);
  const [selectedCards, setSelectedCards] = useState([]);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [imgCards, setImgCards] = useState([]); // 초기 이미지 카드 배열 비우기
  const [filterCard, setFilterCard] = useState([]); // 검색된 카드
  const [searchFunc, setSearchFunc] = useState(""); // 검색
  const [selectedColor, setSelectedColor] = useState(""); // 색깔 필터링
  const [selectedChildCode, setSelectedChildCode] = useState(
      localStorage.getItem("selectedChildCode") || null
  );
  const [viewModalOpen, setViewModalOpen] = useState(false);
  const [selectedCard, setSelectedCard] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // S3에서 이미지를 가져오기 위한 convertToBase64 함수
  const convertToBase64 = useCallback((data, type = "image") => {
    const mimeType = type === "audio" ? "audio/mp3" : "image/png";

    if (typeof data === "string") {
      if (data.startsWith("http://") || data.startsWith("https://")) {
        return data;
      }
      return `data:${mimeType};base64,${data}`;
    } else if (data instanceof Uint8Array) {
      const base64String = btoa(String.fromCharCode.apply(null, data));
      return `data:${mimeType};base64,${base64String}`;
    }

    console.error("Unsupported image data format:", data);
    return "";
  }, []);

  // 카드 데이터를 변환하여 사용
  const transformCard = useCallback(
      (card, type) => {
        const imgSrc =
            type === "default" //디폴트일 경우 처리
                ? convertToBase64(card.cardImage, "image")
                : card.image || convertToBase64(card.cardImage, "image");

        const audioSrc =
            type === "default"
                ? convertToBase64(card.cardVoice, "audio")
                : card.voice;

        if (type === "default") {
          return {
            id: card.defaultCardId,
            imgColor: "#767676",
            imgName: card.cardName,
            imgSrc,
            audioSrc,
            type: "default",
          };
        } else {
          return {
            id: card.aacCardId,
            imgColor: `#${card.color}`,
            imgName: card.cardName,
            imgSrc,
            audioSrc,
            type: "aac",
          };
        }
      },
      [convertToBase64]
  );

  // AWS S3에서 카드 데이터를 가져오기
  const fetchAACCards = useCallback(async () => {
    const constructorId = localStorage.getItem("userId");
    setLoading(true);
    setError(null);

    try {
      const defaultResponse = await axios.get(
          `${process.env.REACT_APP_SERVER_IP}/default-card/get`,
          { params: { constructorId } }
      );

      const defaultCards = defaultResponse.data.map((card) =>
          transformCard(card, "default")
      );

      let aacCards = [];
      if (selectedChildCode) {
        const aacResponse = await axios.get(
            `${process.env.REACT_APP_SERVER_IP}/aac/get`,
            { params: { childCode: selectedChildCode, constructorId } }
        );
        aacCards = aacResponse.data.map((card) => transformCard(card, "aac"));
      }

      setImgCards([...defaultCards, ...aacCards]);
      setFilterCard([...defaultCards, ...aacCards]);
    } catch (error) {
      console.error("Error fetching cards:", error);
      setError("카드를 불러오는 데 실패했습니다.");
    } finally {
      setLoading(false);
    }
  }, [selectedChildCode, transformCard]);

  useEffect(() => {
    fetchAACCards();
  }, [selectedChildCode, fetchAACCards]);

  // 검색 기능 처리
  const handleSearch = (e) => {
    const query = e.target.value.replace(/\s+/g, "");
    setSearchFunc(query);

    const filteredCards = imgCards.filter((card) => {
      const cardName =
          card.type === "default"
              ? translateCardName(card.imgName)
              : card.imgName;

      return cardName
          .replace(/\s+/g, "")
          .toLowerCase()
          .includes(query.toLowerCase());
    });

    setFilterCard(filteredCards);
  };

  const handleSearchClick = () => {
    const filteredCards = imgCards.filter((card) => {
      const cardName =
          card.type === "default"
              ? translateCardName(card.imgName)
              : card.imgName;

      return cardName
          .replace(/\s+/g, "")
          .toLowerCase()
          .includes(searchFunc.toLowerCase());
    });

    setFilterCard(filteredCards);
  };

  const handleEnterKey = (e) => {
    if (e.key === "Enter") {
      handleSearchClick();
    }
  };

  // 색상 필터링 기능
  useEffect(() => {
    if (selectedColor) {
      const filteredByColor = imgCards.filter(
          (card) => card.imgColor === selectedColor
      );
      setFilterCard(filteredByColor);
    } else {
      setFilterCard(imgCards);
    }
  }, [selectedColor, imgCards]);

  const handleColorFiltering = (color) => {
    setSelectedColor(color);
    if (color) {
      const filteredByColor = imgCards.filter(
          (card) => card.imgColor === color
      );
      setFilterCard(filteredByColor); // 색상이 선택된 카드들만 보여주기
    } else {
      setFilterCard(imgCards); // 색상 선택 해제 시 모든 카드 보여주기
    }
  };

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const onCardClick = (card) => {
    console.log("클릭된 카드:", card);
    setSelectedCard(card);
    setViewModalOpen(true);
  };

  const closeViewModal = () => {
    setViewModalOpen(false);
    setSelectedCard(null);
  };

  const handlePrintClick = () => {
    if (selectable) {
      //프린트처리
      printTriggerRef.current.click();
      setSelectable(false);
    } else {
      setSelectable(true);
    }
  };

  const handleSelectCard = (card, isSelected) => {
    setSelectedCards((prevSelectedCards) => {
      if (isSelected) {
        if (
            !prevSelectedCards.some((selectedCard) => selectedCard.id === card.id)
        ) {
          return [...prevSelectedCards, card];
        }
      } else {
        //체크 한번 더 누르면 취소 ㅇㅇ
        return prevSelectedCards.filter(
            (selectedCard) => selectedCard.id !== card.id
        );
      }
      return prevSelectedCards;
    });
  };

  return (
      <>
        <div className="aac-root">
          <div className="aac-top-div">
            <img
                src={SearchImg}
                alt="카드 검색"
                className="search-icon"
                onClick={handleSearchClick}
            />
            <input
                type="text"
                placeholder="검색어를 입력해 주세요."
                className="aac-search__input"
                value={searchFunc}
                onChange={handleSearch}
                onKeyDown={handleEnterKey}
            />
            <SelectColor setColor={handleColorFiltering} />
          </div>
          <div className="aac-btn">
            <button className="add-card-btn" onClick={openModal}>
              <img src={AddCardImg} alt="카드 추가" className="AddCardImg" />
            </button>
            <div className="aac-print-btn" onClick={handlePrintClick}>
              <img src={DeleteAAC} alt="카드 다중 삭제" className="DeleteAAC" />
              <img
                  src={selectable ? MultiPrintImg : PrintImg}
                  alt="프린트 버튼"
                  className={selectable ? "MultiPrintImg" : "PrintImg"}
              />
            </div>
          </div>
          {loading ? (
              <p>Loading...</p>
          ) : error ? (
              <>
                <p>{error}</p>
                <AACList
                    imgCards={filterCard}
                    onCardClick={onCardClick}
                    selectable={selectable}
                    onSelectCard={handleSelectCard}
                />
              </>
          ) : (
              <AACList
                  imgCards={filterCard}
                  onCardClick={onCardClick}
                  selectable={selectable}
                  onSelectCard={handleSelectCard}
              />
          )}
        </div>
        <ReactToPrint
            trigger={() => (
                <button ref={printTriggerRef} style={{ display: "none" }}>
                  프린트
                </button>
            )}
            content={() => printRef.current}
            onAfterPrint={() => setSelectable(false)}
            pageStyle="@media print { size: A4; margin: 10mm; }"
        />
        <div ref={printRef} style={{ display: selectable ? "block" : "none" }}>
          <MultiPrintContainer>
            {selectedCards.map((card) => (
                <MultiPrintBox key={card.id} bgColor={card.imgColor}>
                  <MultiPrintCardImage src={card.imgSrc} alt={card.imgName} />
                  <MultiPrintTitle style={{ fontSize: "15px" }}>
                    {card.imgName}
                  </MultiPrintTitle>                </MultiPrintBox>
            ))}
          </MultiPrintContainer>
        </div>

        {isModalOpen && (
            <CreateAAC
                isModalOpen={isModalOpen}
                closeModal={closeModal}
                fetchAACCards={fetchAACCards}
            />
        )}
        {viewModalOpen && (
            <ViewAAC
                card={selectedCard}
                isModalOpen={viewModalOpen}
                closeModal={closeViewModal}
                fetchAACCards={fetchAACCards}
            />
        )}
      </>
  );
}

export default AAC;