import React from "react";
import { Edit, Trash2 } from "lucide-react";
import StarRating from "./StarRating";
import { Rating } from "../types/Rating";

type RatingCardProps = {
  rating: Rating;
  user: { role: string; email: string } | null;
  onUpdate: (rating: Rating) => void;
  onDelete: (ratingId: number) => void;
};

const RatingCard: React.FC<RatingCardProps> = ({
  rating,
  user,
  onUpdate,
  onDelete,
}) => {
  const isOwner = user?.email === rating.userEmail; // Check if the logged-in user owns the rating
  const isAdmin = user?.role === "ADMIN"; // Check if the logged-in user is an admin

  return (
    <div className="bg-white shadow-sm p-4 rounded-lg border">
      <div className="flex items-center justify-between mb-2">
        <div>
          <h4 className="font-semibold text-gray-800">{rating.userEmail}</h4>
          <StarRating rating={rating.point} />
        </div>

        {/* Buttons */}
        <div className="flex gap-2">
          {/* Update Button (only for the owner) */}
          {isOwner && (
            <button
              onClick={() => onUpdate(rating)}
              className="flex items-center gap-1 px-3 py-1 text-sm font-medium text-blue-600 bg-blue-100 rounded-md hover:bg-blue-200 transition-all"
            >
              Update
            </button>
          )}

          {/* Delete Button (for the owner or admin) */}
          {(isOwner || isAdmin) && (
            <button
              onClick={() => onDelete(rating.id)}
              className="flex items-center gap-1 px-3 py-1 text-sm font-medium text-red-600 bg-red-100 rounded-md hover:bg-red-200 transition-all"
            >
              Delete
            </button>
          )}
        </div>
      </div>

      <p className="text-gray-700 break-words">{rating.description}</p>
    </div>
  );
};

export default RatingCard;
