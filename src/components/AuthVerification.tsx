"use client";

import { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function AuthVerification() {
  const { isAuthenticated, isLoading } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [checkingAuth, setCheckingAuth] = useState(true);

  useEffect(() => {
    if (!isLoading) {
      const currentPath = location.pathname;
      const isAuthPage = currentPath === "/login" || currentPath === "/verify";

      if (isAuthenticated) {
        if (currentPath === "/login" || currentPath === "/verify") {
          navigate("/");
        }
      } else {
        if (!isAuthPage) {
          navigate("/login");
        }
      }

      setCheckingAuth(false);
    }
  }, [isAuthenticated, isLoading, location, navigate]);

  if (isLoading || checkingAuth) {
    return null;
  }

  return null;
}
