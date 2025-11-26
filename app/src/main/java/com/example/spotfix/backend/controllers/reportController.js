import Report from "../models/reportModel.js";
import Admin from "../models/adminModel.js";

// ✅ Create a new report
export const createReport = async (req, res) => {
  try {
    // 🔐 Get username from session
    const username = req.session.user?.username;
    if (!username) {
      return res.status(401).json({
        success: false,
        message: "Login required",
      });
    }

    // 📝 Extract normal fields
    const { description, category } = req.body;

    // 📍 FIX: Extract location from FormData
    const { latitude, longitude } = req.body;


    if (!latitude || !longitude) {
      return res.status(400).json({
        success: false,
        message: "Location data missing",
      });
    }

    // 🖼️ Image URL from multer upload
    const imageUrl = req.file ? req.file.path : "";

    // 🧾 Create report object
    const report = new Report({
      imageUrl,
      description,
      category,
      location: {
        latitude: Number(latitude),
        longitude: Number(longitude),
      },
      username,
      status: "Pending",
    });

    await report.save();

    res.status(201).json({
      success: true,
      report,
    });

  } catch (error) {
    console.error("❌ Error creating report:", error);
    res.status(500).json({
      success: false,
      message: error.message,
    });
  }
};


// ✅ Fetch all reports (for admin dashboard)
export const getAllReports = async (req, res) => {
  try {
    const reports = await Report.find()
      .populate("assignedAdmin")
      .sort({ createdAt: -1 });
    res.status(200).json({ success: true, reports });
  } catch (error) {
    console.error("❌ Error fetching reports:", error);
    res.status(500).json({ success: false, message: error.message });
  }
};

// ✅ Fetch reports assigned to a specific admin
export const getAdminReports = async (req, res) => {
  try {
    const adminId = req.params.id;
    const reports = await Report.find({ assignedAdmin: adminId }).sort({
      createdAt: -1,
    });
    res.status(200).json({ success: true, reports });
  } catch (error) {
    console.error("❌ Error fetching admin reports:", error);
    res.status(500).json({ success: false, message: error.message });
  }
};

// ✅ Update report status (Pending → In Progress → Resolved)
export const updateStatus = async (req, res) => {
  try {
    const { status } = req.body;
    const report = await Report.findByIdAndUpdate(
      req.params.id,
      { status },
      { new: true }
    );
    if (!report) {
      return res.status(404).json({ success: false, message: "Report not found" });
    }
    res.status(200).json({ success: true, report });
  } catch (error) {
    console.error("❌ Error updating status:", error);
    res.status(500).json({ success: false, message: error.message });
  }
};
