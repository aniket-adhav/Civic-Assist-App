import Comment from "../models/commentModel.js";
import Report from "../models/reportModel.js";

export const addComment = async (req, res) => {
  try {
    const { reportId, username, text } = req.body;

    const comment = new Comment({ report: reportId, username, text });
    await comment.save();

    await Report.findByIdAndUpdate(reportId, { $push: { comments: comment._id } });

    res.status(201).json({ success: true, message: "Comment added", comment });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
};
