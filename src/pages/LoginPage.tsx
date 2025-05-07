import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { api } from "../api";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [isRegistering, setIsRegistering] = useState(false); // For popup visibility
  const [registerData, setRegisterData] = useState({
    email: "",
    firstName: "",
    lastName: "",
    phone: "",
  });
  const [registerError, setRegisterError] = useState<string | null>(null);
  const [isRegisterLoading, setIsRegisterLoading] = useState(false);
  const navigate = useNavigate();

  const handleSendOtp = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);

    try {
      const response = await api.auth.login(email, password);

      if (response.statusCode === 200) {
        navigate(`/verify?email=${encodeURIComponent(email)}`);
      } else {
        setError(
          response.message ||
            "Failed to send OTP. Please check your credentials."
        );
      }
    } catch (err) {
      setError("An unexpected error occurred. Please try again.");
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsRegisterLoading(true);
    setRegisterError(null);

    try {
      const response = await api.auth.register(registerData);

      if (response.statusCode === 200) {
        alert(
          "Registration successful! Please check your email for the password."
        );
        setIsRegistering(false); // Close the popup
      } else {
        setRegisterError(
          response.message || "Failed to register. Please try again."
        );
      }
    } catch (err) {
      setRegisterError("An unexpected error occurred. Please try again.");
      console.error(err);
    } finally {
      setIsRegisterLoading(false);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-50 px-4 py-12 sm:px-6 lg:px-8">
      <div className="w-full max-w-md space-y-8 bg-white p-8 rounded-lg shadow-sm">
        {/* Welcome Text */}
        <div className="text-center">
          <h1 className="text-3xl font-extrabold text-indigo-600">
            Welcome to ShopEase
          </h1>
        </div>

        <div>
          <h2 className="mt-6 text-center text-3xl font-bold tracking-tight text-gray-900">
            Sign in to your account
          </h2>
          <p className="mt-2 text-center text-sm text-gray-600">
            Enter your email to receive a one-time password
          </p>
        </div>

        {error && (
          <div className="rounded-md bg-red-50 p-4">
            <div className="flex">
              <div className="ml-3">
                <h3 className="text-sm font-medium text-red-800">Error</h3>
                <div className="mt-2 text-sm text-red-700">
                  <p>{error}</p>
                </div>
              </div>
            </div>
          </div>
        )}

        <form className="mt-8 space-y-6" onSubmit={handleSendOtp}>
          <div className="space-y-4">
            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium text-gray-700 mb-1"
              >
                Email address
              </label>
              <input
                id="email"
                name="email"
                type="email"
                autoComplete="email"
                required
                className="block w-full rounded-md border border-gray-300 px-3 py-2 shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm"
                placeholder="Email address"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium text-gray-700 mb-1"
              >
                Password
              </label>
              <input
                id="password"
                name="password"
                type="password"
                autoComplete="current-password"
                required
                className="block w-full rounded-md border border-gray-300 px-3 py-2 shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </div>

          <div>
            <button
              type="submit"
              disabled={isLoading}
              className="group relative flex w-full justify-center rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 disabled:bg-indigo-400"
            >
              {isLoading ? "Sending..." : "Send OTP"}
            </button>
          </div>
        </form>

        <div className="text-center mt-4">
          <p className="text-sm text-gray-600">Don't have an account?</p>
          <button
            onClick={() => setIsRegistering(true)}
            className="mt-2 text-sm font-medium text-indigo-600 hover:text-indigo-500 bg-transparent border-none shadow-none"
          >
            Register a new account
          </button>
        </div>
      </div>

      {/* Registration Popup */}
      {isRegistering && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
            <h2 className="text-xl font-bold mb-4">Register</h2>
            {registerError && (
              <div className="rounded-md bg-red-50 p-4 mb-4">
                <p className="text-sm text-red-700">{registerError}</p>
              </div>
            )}
            <form
              onSubmit={(e) => {
                handleRegister(e);
                setRegisterData({
                  email: "",
                  firstName: "",
                  lastName: "",
                  phone: "",
                });
              }}
              className="space-y-4"
            >
              <input
                type="email"
                placeholder="Email"
                value={registerData.email}
                onChange={(e) =>
                  setRegisterData({ ...registerData, email: e.target.value })
                }
                className="w-full px-4 py-2 border rounded-md"
                required
                disabled={isRegisterLoading}
              />

              <input
                type="text"
                placeholder="First Name"
                value={registerData.firstName}
                onChange={(e) =>
                  setRegisterData({
                    ...registerData,
                    firstName: e.target.value,
                  })
                }
                className="w-full px-4 py-2 border rounded-md"
                required
                disabled={isRegisterLoading}
              />

              <input
                type="text"
                placeholder="Last Name"
                value={registerData.lastName}
                onChange={(e) =>
                  setRegisterData({ ...registerData, lastName: e.target.value })
                }
                className="w-full px-4 py-2 border rounded-md"
                required
                disabled={isRegisterLoading}
              />

              <input
                type="text"
                placeholder="Phone"
                value={registerData.phone}
                onChange={(e) => {
                  const value = e.target.value.replace(/\D/g, "");
                  setRegisterData({ ...registerData, phone: value });
                }}
                className="w-full px-4 py-2 border rounded-md"
                required
                disabled={isRegisterLoading}
                pattern="\d{10}"
                title="Phone number must be exactly 10 digits"
              />
              <div className="flex justify-end gap-4">
                <button
                  type="button"
                  onClick={() => {
                    setIsRegistering(false);
                    setRegisterData({
                      email: "",
                      firstName: "",
                      lastName: "",
                      phone: "",
                    });
                  }}
                  disabled={isRegisterLoading}
                  className={`px-4 py-2 rounded-md ${
                    isRegisterLoading
                      ? "bg-gray-300 text-gray-500 cursor-not-allowed"
                      : "bg-gray-300 text-gray-800 hover:bg-gray-400"
                  }`}
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={isRegisterLoading}
                  className={`px-4 py-2 rounded-md ${
                    isRegisterLoading
                      ? "bg-green-400 text-white cursor-not-allowed"
                      : "bg-green-600 text-white hover:bg-green-700"
                  }`}
                >
                  {isRegisterLoading ? "Registering..." : "Register"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
