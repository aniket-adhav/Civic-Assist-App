import mongoose from "mongoose";
import Report from "./models/reportModel.js";
import dotenv from "dotenv";
const reports = [
  {
    imageUrl: "/uploads/1762628197467-ggg.jpg",
    description: "Deep potholes on the main road causing accidents and traffic congestion.",
    category: "Potholes",
    location: { latitude: 18.5204, longitude: 73.8567 },
    username: "rahul123",
    status: "Pending",
  },
  {
    imageUrl: "/uploads/1762628197467-ggg.jpg",
    description: "Garbage not collected for several days near the market area.",
    category: "Garbage",
    location: { latitude: 18.5079, longitude: 73.8077 },
    username: "priya_s",
    status: "In Progress",
  },
  {
    imageUrl: "/uploads/1762628197467-ggg.jpg",
    description:
      "Deep potholes on the main road causing accidents and traffic congestion.",
    category: "Potholes",
    location: { latitude: 18.5204, longitude: 73.8567 },
    username: "rahul123",
    status: "Pending",
  },
  {
    imageUrl: "/uploads/1762628197467-ggg.jpg",
    description:
      "Garbage not collected for several days near the market area.",
    category: "Garbage",
    location: { latitude: 18.5079, longitude: 73.8077 },
    username: "priya_s",
    status: "In Progress",
  },
  {
    imageUrl: "/uploads/1762628197467-ggg.jpg",
    description:
      "Streetlights not functioning on the main square, making it unsafe at night.",
    category: "Streetlight",
    location: { latitude: 18.5303, longitude: 73.8478 },
    username: "amit_k",
    status: "Resolved",
  },
  {
    imageUrl: "/uploads/1762628197467-ggg.jpg",
    description:
      "Water leakage from underground pipe creating slippery road surface.",
    category: "Water",
    location: { latitude: 18.5196, longitude: 73.8553 },
    username: "neha_07",
    status: "Pending",
  },
  {
    imageUrl: "/uploads/1762628197467-ggg.jpg",
    description:
      "Loud speaker noise from nearby construction site disturbing residents at night.",
    category: "Noise",
    location: { latitude: 18.5228, longitude: 73.8542 },
    username: "sahil_m",
    status: "In Progress",
  },
  {
    imageUrl: "/uploads/1762628197467-ggg.jpg",
    description:
      "Cracked road surface leading to uneven drive experience and potential damage.",
    category: "Road",
    location: { latitude: 18.5341, longitude: 73.8419 },
    username: "ananya_t",
    status: "Pending",
  },
  {
    imageUrl: "/uploads/1762628197467-ggg.jpg",
    description: "Overflowing dustbin near school attracting stray animals.",
    category: "Garbage",
    location: { latitude: 18.4901, longitude: 73.8275 },
    username: "ravi_89",
    status: "Resolved",
  },
  {
    imageUrl: "/uploads/1762628197467-ggg.jpg",
    description:
      "Multiple potholes near the bus stand slowing down traffic and damaging vehicles.",
    category: "Potholes",
    location: { latitude: 18.5165, longitude: 73.8429 },
    username: "komal_j",
    status: "In Progress",
  },
  
  // ... (add all other reports here)
];

mongoose
  .connect("mongodb+srv://Nayan:d4g0oAbounFrQS0n@cluster0.opqbzcf.mongodb.net/?appName=Cluster0")
  .then(async () => {
    await Report.insertMany(reports);
    console.log("✅ Dummy reports added successfully!");
    mongoose.connection.close();
  })
  .catch((err) => console.error("❌ Error inserting reports:", err));
