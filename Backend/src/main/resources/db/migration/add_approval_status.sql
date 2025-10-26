-- Add approval_status column to restaurants table
ALTER TABLE restaurants ADD COLUMN IF NOT EXISTS approval_status VARCHAR(20) DEFAULT 'PENDING';

-- Set existing restaurants to APPROVED so they remain visible
UPDATE restaurants SET approval_status = 'APPROVED' WHERE approval_status IS NULL;

-- Make the column NOT NULL after setting defaults
ALTER TABLE restaurants ALTER COLUMN approval_status SET NOT NULL;
