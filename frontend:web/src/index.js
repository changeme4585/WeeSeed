import React, { useState, createContext } from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";
import "./index.css";
import Login from "./pages/Login";
import SignInN from "./pages/SignInN";
import SignInP from "./pages/SignInP";
import AAC from "./pages/AAC";
import Video from "./pages/Video";
import Growth from "./pages/Growth";
import Stats from "./pages/Stats";
import AllStats from "./pages/AllStats";
import Settings from "./pages/Settings";
import Nav from "./components/Nav";
import CustomScrollbar from "./components/CustomScrollbar";

export const ChildContext = createContext();

const MainContent = () => {
  const location = useLocation();
  const noNavPaths = ["/", "/sign-in-nok", "/sign-in-path"];

  return (
    <div className="app-container">
      {!noNavPaths.includes(location.pathname) && <Nav />}
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/sign-in-nok" element={<SignInN />} />
        <Route path="/sign-in-path" element={<SignInP />} />
        <Route path="/aac" element={<AAC />} />
        <Route path="/video" element={<Video />} />
        <Route path="/growth-diary" element={<Growth />} />
        <Route path="/stats" element={<Stats />} />
        <Route path="/all-stats" element={<AllStats />} />
        <Route path="/settings" element={<Settings />} />
      </Routes>
    </div>
  );
};

const Main = () => {
  const [selectedChild, setSelectedChild] = useState(null);

  return (
    <ChildContext.Provider value={{ selectedChild, setSelectedChild }}>
      <BrowserRouter>
        <CustomScrollbar autoHide maximalTumbYSize={95}>
          <MainContent />
        </CustomScrollbar>
      </BrowserRouter>
    </ChildContext.Provider>
  );
};

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<Main />);
