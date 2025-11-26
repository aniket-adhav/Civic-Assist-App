export const requireLogin = (req, res, next) => {
  // Check if session exists AND user ID is saved
  if (!req.session || !req.session.user) {
    return res.status(401).json({
      success: false,
      message: "Please log in to continue",
    });
  }

  next();
};
