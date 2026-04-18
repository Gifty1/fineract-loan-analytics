-- Sample microfinance loan data for Fineract Loan Analytics API
-- Run this after the schema is created (spring.jpa.hibernate.ddl-auto=update)

INSERT INTO loans (client_name, branch, loan_amount, disbursed_amount, outstanding_balance, disbursement_date, due_date, status, days_overdue, product_type) VALUES
('Ahmed Al Rashid',    'Dubai Main',     15000.00, 15000.00, 3200.00,  '2024-01-10', '2025-01-10', 'ACTIVE',      0,   'Business Loan'),
('Fatima Hassan',      'Sharjah',        8000.00,  8000.00,  0.00,     '2023-06-15', '2024-06-15', 'CLOSED',      0,   'Personal Loan'),
('Mohammed Al Farsi',  'Abu Dhabi',      25000.00, 25000.00, 18000.00, '2024-03-01', '2025-03-01', 'ACTIVE',      0,   'SME Loan'),
('Sara Al Marzouqi',   'Dubai Main',     5000.00,  5000.00,  5000.00,  '2023-11-20', '2024-05-20', 'OVERDUE',     120, 'Personal Loan'),
('Khalid Bin Zayed',   'RAK',            12000.00, 12000.00, 4500.00,  '2024-02-14', '2025-02-14', 'ACTIVE',      0,   'Business Loan'),
('Aisha Al Nuaimi',    'Ajman',          6500.00,  6500.00,  6500.00,  '2023-09-05', '2024-03-05', 'OVERDUE',     45,  'Personal Loan'),
('Hassan Al Bloushi',  'Abu Dhabi',      30000.00, 30000.00, 12000.00, '2024-01-22', '2026-01-22', 'ACTIVE',      0,   'SME Loan'),
('Mariam Al Ketbi',    'Sharjah',        9000.00,  9000.00,  9000.00,  '2023-07-30', '2024-01-30', 'OVERDUE',     95,  'Business Loan'),
('Omar Al Hamdan',     'Dubai Main',     18000.00, 18000.00, 2000.00,  '2023-03-10', '2025-03-10', 'ACTIVE',      0,   'Business Loan'),
('Noura Bin Sultan',   'RAK',            4000.00,  4000.00,  0.00,     '2023-05-01', '2024-05-01', 'CLOSED',      0,   'Personal Loan'),
('Youssef Al Mazrouei','Abu Dhabi',      22000.00, 22000.00, 22000.00, '2022-12-01', '2023-12-01', 'WRITTEN_OFF', 365, 'SME Loan'),
('Hind Al Suwaidi',    'Dubai Main',     7500.00,  7500.00,  1200.00,  '2024-04-01', '2025-04-01', 'ACTIVE',      0,   'Personal Loan'),
('Saeed Al Qasimi',    'Ajman',          11000.00, 11000.00, 7800.00,  '2024-02-28', '2025-08-28', 'ACTIVE',      0,   'Business Loan'),
('Latifa Al Muhairi',  'Sharjah',        3500.00,  3500.00,  3500.00,  '2023-10-15', '2024-04-15', 'OVERDUE',     60,  'Personal Loan'),
('Rashid Al Ketbi',    'RAK',            16000.00, 16000.00, 5000.00,  '2023-08-20', '2025-08-20', 'ACTIVE',      0,   'Business Loan');
