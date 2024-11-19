import React from "react";
import { motion } from "framer-motion";

const ModalAnimation = ({ children, isOpen }) => {
  const variants = {
    hidden: { opacity: 0.1, y: 20, scale: 1 },
    visible: { opacity: 1, y: 0, scale: 1 },
  };

  return (
    <motion.div
      initial="hidden"
      animate={isOpen ? "visible" : "hidden"}
      variants={variants}
      transition={{ duration: 0.4, ease: "easeOut" }}
    >
      {children}
    </motion.div>
  );
};

export default ModalAnimation;
