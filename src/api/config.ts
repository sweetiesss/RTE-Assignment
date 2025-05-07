export const API_BASE_URL = "http://localhost:8801";

export function getAuthToken() {
  return localStorage.getItem("accessToken");
}

export async function fetchWithAuth(
  endpoint: string,
  options: RequestInit = {}
) {
  const token = getAuthToken();

  const headers = {
    "Content-Type": "application/json",
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...options.headers,
  };

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
    credentials: "include",
  });

  if (!response.ok) {
    if (response.status === 401) {
      localStorage.removeItem("accessToken");
      window.location.href = "/login";
      throw new Error("Unauthorized");
    }

    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.message || "API request failed");
  }

  return response.json();
}
