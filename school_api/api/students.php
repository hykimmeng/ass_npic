<?php
declare(strict_types=1);

require_once __DIR__ . '/../includes/bootstrap.php';
require_login();

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $stmt = $pdo->query(
        'SELECT student_id, name, gender, DATE_FORMAT(date_of_birth, "%Y-%m-%d") AS date_of_birth, `class`, phone, address FROM students ORDER BY id DESC'
    );
    $rows = $stmt->fetchAll();
    echo json_encode($rows, JSON_UNESCAPED_UNICODE);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $body = read_json_body();
    $studentId = trim((string) ($body['student_id'] ?? ''));
    $name = trim((string) ($body['name'] ?? ''));
    $gender = strtolower(trim((string) ($body['gender'] ?? '')));
    $dob = trim((string) ($body['date_of_birth'] ?? ''));
    $class = isset($body['class']) ? trim((string) $body['class']) : '';
    $phone = isset($body['phone']) ? trim((string) $body['phone']) : '';
    $address = isset($body['address']) ? trim((string) $body['address']) : '';

    if ($studentId === '' || $name === '' || !in_array($gender, ['male', 'female'], true)) {
        json_response(400, ['status' => 'error', 'message' => 'student_id, name, and gender (male/female) are required']);
    }

    $dobVal = $dob === '' ? null : $dob;
    $classVal = $class === '' ? null : $class;
    $phoneVal = $phone === '' ? null : $phone;
    $addressVal = $address === '' ? null : $address;

    try {
        $stmt = $pdo->prepare(
            'INSERT INTO students (student_id, name, gender, date_of_birth, `class`, phone, address) VALUES (?, ?, ?, ?, ?, ?, ?)'
        );
        $stmt->execute([$studentId, $name, $gender, $dobVal, $classVal, $phoneVal, $addressVal]);
    } catch (PDOException $e) {
        if ((int) $e->errorInfo[1] === 1062) {
            json_response(409, ['status' => 'error', 'message' => 'Student ID already exists']);
        }
        json_response(500, ['status' => 'error', 'message' => 'Could not save student']);
    }

    json_response(200, ['status' => 'success', 'message' => 'Student registered', 'data' => $studentId]);
}

json_response(405, ['status' => 'error', 'message' => 'Method not allowed']);
