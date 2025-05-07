import React, { useState, useEffect } from "react";
import { api } from "../api";
import Navbar from "../components/Navbar";

type User = {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  phone: string;
  role: "ADMIN" | "CUSTOMER";
  deleted: string;
  createOn: string | null;
  lastUpdateOn: string;
};

export default function UserManagementPage() {
  const [users, setUsers] = useState<User[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [editingUser, setEditingUser] = useState<User | null>(null);
  const [searchInput, setSearchInput] = useState("");
  const [confirmingDeleteUserId, setConfirmingDeleteUserId] = useState<
    number | null
  >(null);
  const [formData, setFormData] = useState({
    email: "",
    firstName: "",
    lastName: "",
    phone: "",
  });
  useEffect(() => {
    fetchUsers();
  }, [currentPage]);

  const fetchUsers = async (
    search: string = "",
    resetPage: boolean = false
  ) => {
    if (resetPage) {
      setCurrentPage(0);
    }
    setIsLoading(true);
    try {
      const response = await api.users.getAll({
        page: resetPage ? 0 : currentPage,
        size: 8,
        sort: "id",
        search,
      });

      setUsers(response.content);
      setTotalPages(response.pageable.totalPages);
    } catch (err) {
      console.error("Failed to fetch users:", err);
      setError("Failed to load users.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleEditUser = async (userId: number) => {
    try {
      const response = await api.users.getById(userId);
      const user = response.data;

      setEditingUser(user);
      setFormData({
        email: user.email,
        firstName: user.firstName,
        lastName: user.lastName,
        phone: user.phone,
      });
    } catch (err) {
      console.error("Failed to fetch user details:", err);
      alert("Failed to fetch user details.");
    }
  };

  const handleDeleteUser = async (userId: number) => {
    try {
      await api.users.delete(userId);

      setUsers((prevUsers) =>
        prevUsers.map((user) =>
          user.id === userId ? { ...user, deleted: "Deleted" } : user
        )
      );

      setConfirmingDeleteUserId(null);
    } catch (err) {
      console.error("Failed to delete user:", err);
    }
  };
  const handleRestoreUser = async (userId: number) => {
    try {
      await api.users.restore(userId);
      setUsers((prevUsers) =>
        prevUsers.map((user) =>
          user.id === userId ? { ...user, deleted: "Not Deleted" } : user
        )
      );
    } catch (err) {
      console.error("Failed to restore user:", err);
    }
  };
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!editingUser) return;

    try {
      await api.users.update(editingUser.id, formData);

      setUsers((prevUsers) =>
        prevUsers.map((user) =>
          user.id === editingUser.id ? { ...user, ...formData } : user
        )
      );

      setEditingUser(null);
      setFormData({
        email: "",
        firstName: "",
        lastName: "",
        phone: "",
      });
    } catch (err) {
      console.error("Failed to update user:", err);
      alert("Failed to update user.");
    }
  };
  const handleCancelEdit = () => {
    setEditingUser(null);
    setFormData({
      email: "",
      firstName: "",
      lastName: "",
      phone: "",
    });
  };
  return (
    <div className="min-h-screen flex flex-col bg-gray-50">
      <Navbar />
      <main className="flex-1 container mx-auto px-4 py-8">
        <h1 className="text-3xl font-bold mb-8">User Management</h1>
        <div className="mb-4 flex items-center">
          <input
            type="text"
            placeholder="Search by email"
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
            className="flex-1 px-4 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
          <button
            onClick={() => {
              setSearchInput("");
              fetchUsers("", true);
            }}
            className="ml-2 px-4 py-2 bg-gray-300 text-gray-800 rounded-md hover:bg-gray-400"
          >
            Reset
          </button>
          <button
            onClick={() => fetchUsers(searchInput, true)}
            className="ml-2 px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700"
          >
            Search
          </button>
        </div>
        {isLoading ? (
          <div className="text-center py-12">
            <div className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-indigo-600 border-r-transparent"></div>
            <p className="mt-4 text-gray-600">Loading users...</p>
          </div>
        ) : error ? (
          <div className="text-red-600 text-center">{error}</div>
        ) : (
          <div className="bg-white p-6 rounded-lg shadow-sm border">
            <table className="w-full border-collapse">
              <thead>
                <tr className="bg-gray-100">
                  <th className="text-left px-4 py-2 border">ID</th>
                  <th className="text-left px-4 py-2 border">Email</th>
                  <th className="text-left px-4 py-2 border">First Name</th>
                  <th className="text-left px-4 py-2 border">Last Name</th>
                  <th className="text-left px-4 py-2 border">Phone</th>
                  <th className="text-left px-4 py-2 border">Deleted</th>
                  <th className="text-left px-4 py-2 border">Actions</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <tr key={user.id} className="hover:bg-gray-50">
                    <td className="px-4 py-2 border">{user.id}</td>
                    <td className="px-4 py-2 border">{user.email}</td>
                    <td className="px-4 py-2 border">{user.firstName}</td>
                    <td className="px-4 py-2 border">{user.lastName}</td>
                    <td className="px-4 py-2 border">{user.phone}</td>
                    <td className="px-4 py-2 border">{user.deleted}</td>
                    <td className="px-4 py-2 border" style={{ width: "200px" }}>
                      {confirmingDeleteUserId === user.id ? (
                        <div className="flex items-center space-x-2">
                          <button
                            onClick={() => handleDeleteUser(user.id)}
                            className="px-3 py-1 bg-red-600 text-white rounded-md hover:bg-red-700"
                          >
                            Confirm
                          </button>
                          <button
                            onClick={() => setConfirmingDeleteUserId(null)}
                            className="px-3 py-1 bg-gray-300 text-gray-800 rounded-md hover:bg-gray-400"
                          >
                            Cancel
                          </button>
                        </div>
                      ) : (
                        <div className="flex items-center space-x-2">
                          <button
                            onClick={() => handleEditUser(user.id)}
                            className="px-3 py-1 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                            disabled={confirmingDeleteUserId !== null}
                          >
                            Edit
                          </button>
                          {user.deleted === "Not Deleted" ? (
                            <button
                              onClick={() => setConfirmingDeleteUserId(user.id)}
                              className="px-3 py-1 bg-red-600 text-white rounded-md hover:bg-red-700"
                            >
                              Delete
                            </button>
                          ) : (
                            <button
                              onClick={() => handleRestoreUser(user.id)}
                              className="px-3 py-1 bg-green-600 text-white rounded-md hover:bg-green-700"
                            >
                              Restore
                            </button>
                          )}
                        </div>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            {editingUser && (
              <div className="bg-white p-6 rounded-lg shadow-sm border mt-8">
                <h2 className="text-xl font-semibold mb-4">Edit User</h2>
                <form onSubmit={handleSubmit} className="space-y-4">
                  <div>
                    <label
                      htmlFor="email"
                      className="block text-sm font-medium text-gray-700 mb-1"
                    >
                      Email
                    </label>
                    <input
                      type="email"
                      id="email"
                      value={formData.email}
                      onChange={(e) =>
                        setFormData({ ...formData, email: e.target.value })
                      }
                      className="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2 border"
                      required
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="firstName"
                      className="block text-sm font-medium text-gray-700 mb-1"
                    >
                      First Name
                    </label>
                    <input
                      type="text"
                      id="firstName"
                      value={formData.firstName}
                      onChange={(e) =>
                        setFormData({ ...formData, firstName: e.target.value })
                      }
                      className="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2 border"
                      required
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="lastName"
                      className="block text-sm font-medium text-gray-700 mb-1"
                    >
                      Last Name
                    </label>
                    <input
                      type="text"
                      id="lastName"
                      value={formData.lastName}
                      onChange={(e) =>
                        setFormData({ ...formData, lastName: e.target.value })
                      }
                      className="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2 border"
                      required
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="phone"
                      className="block text-sm font-medium text-gray-700 mb-1"
                    >
                      Phone
                    </label>
                    <input
                      type="text"
                      id="phone"
                      value={formData.phone}
                      onChange={(e) =>
                        setFormData({ ...formData, phone: e.target.value })
                      }
                      className="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2 border"
                      required
                    />
                  </div>
                  <div className="flex space-x-3">
                    <button
                      type="submit"
                      className="inline-flex justify-center rounded-md border border-transparent bg-indigo-600 py-2 px-4 text-sm font-medium text-white shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                    >
                      Save
                    </button>
                    <button
                      type="button"
                      onClick={handleCancelEdit}
                      className="inline-flex justify-center rounded-md border border-gray-300 bg-white py-2 px-4 text-sm font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                    >
                      Cancel
                    </button>
                  </div>
                </form>
              </div>
            )}
            {/* Pagination Controls */}
            {users.length > 0 && (
              <div className="flex justify-between items-center mt-4">
                <button
                  onClick={() =>
                    setCurrentPage((prev) => Math.max(prev - 1, 0))
                  }
                  disabled={currentPage === 0}
                  className="px-4 py-2 bg-gray-300 text-gray-800 rounded-md hover:bg-gray-400 disabled:opacity-50"
                >
                  Previous
                </button>
                <span>
                  Page {currentPage + 1} of {totalPages}
                </span>
                <button
                  onClick={() =>
                    setCurrentPage((prev) => Math.min(prev + 1, totalPages - 1))
                  }
                  disabled={currentPage === totalPages - 1}
                  className="px-4 py-2 bg-gray-300 text-gray-800 rounded-md hover:bg-gray-400 disabled:opacity-50"
                >
                  Next
                </button>
              </div>
            )}
          </div>
        )}
      </main>
    </div>
  );
}
