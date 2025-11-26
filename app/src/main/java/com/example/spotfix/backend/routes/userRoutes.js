import express from "express";
import {
  registerUser,
  loginUser,
  logoutUser,
  getCurrentUser,
  getUserReports,
} from "../controllers/userController.js";
import { requireLogin } from "../middlewares/authMiddleware.js";

const router = express.Router();

router.post("/register", registerUser);
router.post("/login", loginUser);
router.post("/logout", logoutUser);
router.get("/me",getCurrentUser);
router.get("/:username/reports", requireLogin, getUserReports);

export default router;
