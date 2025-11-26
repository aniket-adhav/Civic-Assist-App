import Admin from "../models/adminModel.js";
import bcrypt from "bcryptjs";

// ✅ Register Admin
export const registerAdmin = async (req, res) => {
  try {
    const { name, department, email, password } = req.body;
    const existing = await Admin.findOne({ email });
    if (existing) {
      return res.status(400).json({ success: false, message: "Admin already exists" });
    }

    const admin = new Admin({ name, department, email, password });
    await admin.save();
    res.status(201).json({ success: true, message: "Admin registered successfully" });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
};

// ✅ Login Admin
export const loginAdmin = async (req, res) => {
  try {
    const { email, password } = req.body;

    const admin = await Admin.findOne({ email });
    if (!admin) return res.status(400).json({ success: false, message: "Invalid email or password" });

    const isMatch = await admin.comparePassword(password);
    if (!isMatch) return res.status(400).json({ success: false, message: "Invalid email or password" });

    // Save admin ID in session
    req.session.adminId = admin._id;
    res.status(200).json({
      success: true,
      message: "Login successful",
      admin: { id: admin._id, name: admin.name, department: admin.department, email: admin.email },
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
};

// ✅ Logout Admin
export const logoutAdmin = (req, res) => {
  req.session.destroy((err) => {
    if (err) return res.status(500).json({ success: false, message: "Logout failed" });
    res.clearCookie("connect.sid");
    res.json({ success: true, message: "Logged out successfully" });
  });
};

// ✅ Get logged-in admin profile
export const getAdminProfile = async (req, res) => {
  if (!req.session.adminId) {
    return res.status(401).json({ success: false, message: "Not logged in" });
  }

  const admin = await Admin.findById(req.session.adminId).select("-password");
  if (!admin) return res.status(404).json({ success: false, message: "Admin not found" });

  res.status(200).json({ success: true, admin });
};

// ✅ (Optional) Get all admins (for super admin dashboard)
export const getAllAdmins = async (req, res) => {
  try {
    const admins = await Admin.find().select("-password");
    res.status(200).json({ success: true, admins });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
};
