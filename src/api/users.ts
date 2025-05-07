// src/api/users.ts
import { fetchWithAuth } from "./config";

export const users = {
  getAll: ({
    page,
    size,
    sort,
    search = "",
  }: {
    page: number;
    size: number;
    sort: string;
    search?: string;
  }) =>
    fetchWithAuth(
      `/admin/users?page=${page}&size=${size}&sort=${sort}&search=${search}`
    ),

  getById: (userId: number) => fetchWithAuth(`/admin/users/${userId}`),

  update: (userId: number, userData: any) =>
    fetchWithAuth(`/admin/users/${userId}`, {
      method: "PUT",
      body: JSON.stringify(userData),
    }),

  delete: (userId: number) =>
    fetchWithAuth(`/admin/users/${userId}`, {
      method: "DELETE",
    }),

  restore: (userId: number) =>
    fetchWithAuth(`/admin/users/${userId}/restore`, {
      method: "PATCH",
    }),
};
