import mongoose from "mongoose";

const reportSchema = new mongoose.Schema(
  {
    imageUrl: { type: String },
    description: { type: String, required: true },
    category: {
      type: String,
      enum: ["Garbage", "Potholes", "Streetlight", "Water", "Noise","Road"],
      required: true,
    },
    location: {
      latitude: { type: Number, required: true },
      longitude: { type: Number, required: true },
    },
    username: { type: String, required: true },
    status: {
      type: String,
      enum: ["Pending", "In Progress", "Resolved"],
      default: "Pending",
    },
    assignedAdmin: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "Admin", // reference to admin who handles this report
    },
  },
  { timestamps: true }
);

const Report = mongoose.model("Report", reportSchema);
export default Report;
