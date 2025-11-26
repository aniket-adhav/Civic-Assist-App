import express from "express";
import {
  registerAdmin,
  loginAdmin,
  logoutAdmin,
  getAdminProfile,
  getAllAdmins,
} from "../controllers/adminController.js";

const router = express.Router();

router.post("/register", registerAdmin);
router.post("/login", loginAdmin);
router.post("/logout", logoutAdmin);
router.get("/profile", getAdminProfile); // session-based
router.get("/", getAllAdmins);

export default router;
