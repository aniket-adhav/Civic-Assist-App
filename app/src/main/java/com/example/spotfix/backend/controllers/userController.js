import User from "../models/userModel.js";
import Report from "../models/reportModel.js";
import bcrypt from "bcryptjs";

export const registerUser = async (req, res) => {
  try {
    const { username, email, password } = req.body;

    // 1️⃣ Check if the email already exists
    const existingUser = await User.findOne({ email });
    if (existingUser) {
      return res
        .status(400)
        .json({ success: false, message: "Email already exists" });
    }

    // 2️⃣ Hash the password
    const hashedPassword = await bcrypt.hash(password, 10);

    // 3️⃣ Create and save the new user
    const user = new User({ username, email, password: hashedPassword });
    await user.save();

    // 4️⃣ Automatically log the user in (create session)
    req.session.user = {
      id: user._id,
      username: user.username,
      email: user.email,
    };

    // 5️⃣ Respond with success + session info
    res.status(201).json({
      success: true,
      message: "User registered & logged in",
      user: req.session.user,
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
};


export const loginUser = async (req, res) => {
  try {
    const { email, password } = req.body;

    const user = await User.findOne({ email });
    if (!user) return res.status(400).json({ success: false, message: "User not found" });

    const isMatch = await bcrypt.compare(password, user.password);
    if (!isMatch) return res.status(400).json({ success: false, message: "Invalid credentials" });

    req.session.user = { id: user._id, username: user.username, email: user.email };
    res.status(200).json({ success: true, message: "Login successful", user: req.session.user });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
};

// 🚪 Logout
export const logoutUser = (req, res) => {
  req.session.destroy((err) => {
    if (err) return res.status(500).json({ success: false, message: "Logout failed" });
    res.clearCookie("connect.sid");
    res.status(200).json({ success: true, message: "Logged out" });
  });
};

// 👤 Get current logged user
export const getCurrentUser = (req, res) => {
  if (req.session.user) {
    return res.json({ success: true, user: req.session.user });
  }
  res.status(401).json({ success: false, message: "Not logged in" });
};

export const getUserReports = async (req, res) => {
  try {
    const { username } = req.params;
    if (req.session.user.username !== username) {
      return res.status(403).json({ success: false, message: "Access denied" });
    }
    const reports = await Report.find({ username });
    res.status(200).json({ success: true, reports });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
};
