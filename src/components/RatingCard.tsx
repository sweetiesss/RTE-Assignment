import React from "react";
import { Edit, Trash2 } from "lucide-react";
import StarRating from "./StarRating";

import { Rating } from "../types/Rating";
import { User } from "../types/User";

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
  return (
    <div className="bg-white shadow-sm p-4 rounded-lg border">
      <div className="flex items-center justify-between mb-2">
        <div>
          {/* User Email */}
          <p className="text-sm font-medium text-gray-800">
            {rating.userEmail}
          </p>
          {/* Star Rating */}
          <StarRating rating={rating.point} />
        </div>

        {/* Update and Delete Buttons */}
        {(user?.role === "ADMIN" || user?.email === rating.userEmail) && (
          <div className="flex gap-2">
            {/* Update Button */}
            <button
              onClick={() => onUpdate(rating)}
              className="flex items-center gap-1 px-3 py-1 text-sm font-medium text-blue-600 bg-blue-100 rounded-md hover:bg-blue-200 transition-all"
            >
              <Edit className="w-4 h-4" />
              Update
            </button>

            {/* Delete Button */}
            <button
              onClick={() => onDelete(rating.id)}
              className="flex items-center gap-1 px-3 py-1 text-sm font-medium text-red-600 bg-red-100 rounded-md hover:bg-red-200 transition-all"
            >
              <Trash2 className="w-4 h-4" />
              Delete
            </button>
          </div>
        )}
      </div>
      <p className="text-gray-700">{rating.description}</p>
    </div>
  );
};

export default RatingCard;
