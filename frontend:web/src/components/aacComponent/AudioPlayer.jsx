import React, { useState, useRef, useEffect } from "react";
import styled from "styled-components";
import playAAC from "../../images/svg/play-icon.svg";

const AudioPlayerContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 310px;
  height: 78px;
  margin-top: 20px;
  position: relative;
`;

const AudioPlayButton = styled.button`
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 24px;
  position: absolute;
  left: 130px;
  transform: translateY(-80%);
  pointer-events: ${(props) => (props.disabled ? "none" : "auto")};
`;

const ProgressBar = styled.div`
  width: 100%;
  height: 5px;
  background-color: #767676;
  margin-top: 12px;
  position: relative;
`;

const Progress = styled.div`
  width: ${(props) => props.progress}%;
  height: 100%;
  background-color: #ffce1b;
  position: absolute;
  top: 0;
  left: 0;
`;

const TimeContainer = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  font-family: "Pretendard-Regular";
  position: absolute;
  bottom: 30px;
  left: 0;
  padding: 0px 3px;
`;

const AudioPlayer = ({ audioSrc }) => {
  const [isPlaying, setIsPlaying] = useState(false);
  const [progress, setProgress] = useState(0);
  //const [currentTime, setCurrentTime] = useState("0:00");
  //const [audioDuration, setAudioDuration] = useState("0:00");
  const audioRef = useRef(null);

  useEffect(() => {
    const audio = audioRef.current;
    /*{
    const setDuration = () => {
      const duration = audio.duration;

      if (!isNaN(duration) && duration > 0 && duration !== Infinity) {
        const formattedDuration = formatTime(duration);
        setAudioDuration(formattedDuration);
        onLoadedMetadata(formattedDuration); //ViewAAC로 보내기
      } else {
        setAudioDuration("Unknown");
      }
    };
    }
    const handleCanPlayThrough = () => {
      setDuration();
    };
    if (audio.readyState >= 1) {
      setDuration();
    }
    */
    const updateProgress = () => {
      if (audio.duration) {
        const currentProgress = (audio.currentTime / audio.duration) * 100;
        setProgress(currentProgress);
      }
    };
    audio.addEventListener("timeupdate", updateProgress);
    audio.addEventListener("ended", () => {
      setIsPlaying(false);
    });

    return () => {
      audio.removeEventListener("timeupdate", updateProgress);
    };
  }, [audioSrc]);
  /* 
  const formatTime = (time) => {
    const minutes = Math.floor(time / 60);
    const seconds = Math.floor(time % 60)
      .toString()
      .padStart(2, "0");
    return `${minutes}:${seconds}`;
  };
*/

  const togglePlay = () => {
    const audio = audioRef.current;
    if (isPlaying) {
      audio.pause();
    } else {
      audio.play();
    }
    setIsPlaying(!isPlaying);
  };

  return (
    <AudioPlayerContainer>
      <AudioPlayButton onClick={togglePlay} disabled={isPlaying}>
        <img src={playAAC} alt="Play Audio" />
      </AudioPlayButton>
      <ProgressBar>
        <Progress progress={progress} />
      </ProgressBar>
      <TimeContainer>
        {/*
        <Time>{currentTime}</Time>
        <Time>{audioDuration}</Time>
        */}
      </TimeContainer>

      <audio ref={audioRef} src={audioSrc} preload="auto" />
    </AudioPlayerContainer>
  );
};

export default AudioPlayer;
