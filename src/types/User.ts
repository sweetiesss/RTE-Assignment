export type User = {
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
