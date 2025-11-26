import express from "express";
import multer from "multer";
import {
  createReport,
  getAllReports,
  getAdminReports,
  updateStatus,
} from "../controllers/reportController.js";
import { requireLogin } from "../middlewares/authMiddleware.js";
import upload from "../middlewares/upload.js";

const router = express.Router();

// 🔹 Configure multer storage to keep original filenames
// const storage = multer.diskStorage({
//   destination: (req, file, cb) => {
//     cb(null, "uploads/");
//   },
//   filename: (req, file, cb) => {
//     const uniqueName = Date.now() + "-" + file.originalname;
//     cb(null, uniqueName);
//   },
// });

// const upload = multer({ storage });

// 🧾 Only logged-in users can create a report
router.post("/", requireLogin, upload.single("imageUrl"), createReport);

// 🌍 Anyone can view all reports (public)
router.get("/", getAllReports);

// 🧑‍💼 Admin can view assigned reports
router.get("/admin/:id", getAdminReports);

// 🔄 Admin can update status
router.put("/:id/status", updateStatus);

export default router;
