import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom"; // Import useLocation
import { useAuth } from "../context/AuthContext";
import { User, Menu, X, Search } from "react-feather";
import { api } from "../api";

export default function Navbar() {
  const { user, isAuthenticated, logout: contextLogout } = useAuth();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isSearchOpen, setIsSearchOpen] = useState(false);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [dropdownTimeout, setDropdownTimeout] = useState<NodeJS.Timeout | null>(
    null
  );

  const location = useLocation(); // Get the current route

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const toggleSearch = () => {
    setIsSearchOpen(!isSearchOpen);
  };

  const logout = async () => {
    try {
      await api.auth.logout();
      contextLogout();
      window.location.href = "/login";
    } catch (error) {
      console.error("Logout failed:", error);
    }
  };

  const handleMouseEnter = () => {
    if (dropdownTimeout) {
      clearTimeout(dropdownTimeout);
    }
    setIsDropdownOpen(true);
  };

  const handleMouseLeave = () => {
    const timeout = setTimeout(() => {
      setIsDropdownOpen(false);
    }, 200);
    setDropdownTimeout(timeout);
  };

  const isActive = (path: string) => location.pathname === path; // Check if the current route matches the path

  return (
    <header className="bg-white shadow-sm sticky top-0 z-50">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link to="/" className="text-2xl font-bold text-indigo-600">
            ShopEase
          </Link>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex items-center space-x-8">
            <Link
              to="/"
              className={`text-sm font-medium transition-colors ${
                isActive("/")
                  ? "text-indigo-600 font-bold"
                  : "hover:text-indigo-600"
              }`}
            >
              Home
            </Link>
            <Link
              to="/products"
              className={`text-sm font-medium transition-colors ${
                isActive("/products")
                  ? "text-indigo-600 font-bold"
                  : "hover:text-indigo-600"
              }`}
            >
              Products
            </Link>
            {user?.role === "ADMIN" && (
              <>
                <Link
                  to="/categories"
                  className={`text-sm font-medium transition-colors ${
                    isActive("/categories")
                      ? "text-indigo-600 font-bold"
                      : "hover:text-indigo-600"
                  }`}
                >
                  Categories
                </Link>
                <Link
                  to="/users"
                  className={`text-sm font-medium transition-colors ${
                    isActive("/users")
                      ? "text-indigo-600 font-bold"
                      : "hover:text-indigo-600"
                  }`}
                >
                  Users
                </Link>
              </>
            )}
          </nav>

          {/* Actions */}
          <div className="flex items-center space-x-4">
            {/* Search Button */}
            <button
              onClick={toggleSearch}
              className="p-2 text-gray-600 hover:text-indigo-600 transition-colors"
              aria-label="Search"
            >
              <Search size={20} />
            </button>

            {/* User Menu */}
            {isAuthenticated ? (
              <div
                className="relative"
                onMouseEnter={handleMouseEnter}
                onMouseLeave={handleMouseLeave}
              >
                <button className="flex items-center space-x-1 p-2 text-gray-600 hover:text-indigo-600 transition-colors">
                  <User size={20} />
                  <span className="text-sm hidden lg:inline-block">
                    {user?.email}
                  </span>
                </button>
                {isDropdownOpen && (
                  <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1">
                    <button
                      onClick={logout}
                      className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                    >
                      Logout
                    </button>
                  </div>
                )}
              </div>
            ) : (
              <Link
                to="/login"
                className="inline-flex items-center text-sm font-medium bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 transition-colors"
              >
                Login
              </Link>
            )}

            {/* Mobile Menu Button */}
            <button
              onClick={toggleMenu}
              className="p-2 rounded-md text-gray-600 md:hidden"
              aria-label="Toggle menu"
            >
              {isMenuOpen ? <X size={24} /> : <Menu size={24} />}
            </button>
          </div>
        </div>
      </div>
    </header>
  );
}
