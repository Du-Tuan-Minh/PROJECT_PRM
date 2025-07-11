// NEW FILE: app/src/main/java/com/example/project_prm/DataManager/SearchManager/DiseaseSearchManager.java
package com.example.project_prm.DataManager.SearchManager;

import android.content.Context;
import com.example.project_prm.DataManager.DAO.DiseaseDAO;
import com.example.project_prm.DataManager.DatabaseHelper;
import com.example.project_prm.DataManager.Entity.Disease;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager class for Disease Search functionality (Chức năng 6)
 * Handles all disease search logic for Tung's features
 */
public class DiseaseSearchManager {
    private DiseaseDAO diseaseDAO;
    private DatabaseHelper dbHelper;

    public DiseaseSearchManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        diseaseDAO = new DiseaseDAO(dbHelper.getReadableDatabase());
    }

    // ========== SEARCH METHODS ==========

    /**
     * Search diseases by symptoms
     */
    public List<Disease> searchBySymptoms(String symptoms) {
        if (symptoms == null || symptoms.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Split symptoms into keywords
        String[] keywords = symptoms.toLowerCase().split("[,\\s]+");
        List<Disease> results = new ArrayList<>();

        for (String keyword : keywords) {
            if (keyword.length() > 2) { // Only search keywords with 3+ characters
                List<Disease> matches = diseaseDAO.findDiseasesBySymptoms(keyword.trim());

                // Add matches that aren't already in results
                for (Disease disease : matches) {
                    if (!containsDisease(results, disease.getId())) {
                        results.add(disease);
                    }
                }
            }
        }

        return rankByRelevance(results, symptoms);
    }

    /**
     * Search diseases by name
     */
    public List<Disease> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<Disease> allDiseases = diseaseDAO.getAllDiseases();
        List<Disease> results = new ArrayList<>();

        String searchTerm = name.toLowerCase().trim();

        for (Disease disease : allDiseases) {
            if (disease.getName().toLowerCase().contains(searchTerm)) {
                results.add(disease);
            }
        }

        return results;
    }

    /**
     * Get all diseases with pagination
     */
    public List<Disease> getAllDiseases(int offset, int limit) {
        List<Disease> allDiseases = diseaseDAO.getAllDiseases();

        int start = Math.min(offset, allDiseases.size());
        int end = Math.min(offset + limit, allDiseases.size());

        if (start >= end) {
            return new ArrayList<>();
        }

        return allDiseases.subList(start, end);
    }

    /**
     * Get disease details by ID
     */
    public Disease getDiseaseDetails(int diseaseId) {
        return diseaseDAO.getDiseaseById(diseaseId);
    }

    /**
     * Search with multiple filters
     */
    public List<Disease> searchWithFilters(String name, String symptoms, String causes) {
        List<Disease> results = new ArrayList<>();
        List<Disease> allDiseases = diseaseDAO.getAllDiseases();

        for (Disease disease : allDiseases) {
            boolean matches = true;

            // Filter by name
            if (name != null && !name.trim().isEmpty()) {
                if (!disease.getName().toLowerCase().contains(name.toLowerCase().trim())) {
                    matches = false;
                }
            }

            // Filter by symptoms
            if (symptoms != null && !symptoms.trim().isEmpty() && matches) {
                if (!disease.getSymptoms().toLowerCase().contains(symptoms.toLowerCase().trim())) {
                    matches = false;
                }
            }

            // Filter by causes
            if (causes != null && !causes.trim().isEmpty() && matches) {
                if (!disease.getCauses().toLowerCase().contains(causes.toLowerCase().trim())) {
                    matches = false;
                }
            }

            if (matches) {
                results.add(disease);
            }
        }

        return results;
    }

    /**
     * Get popular/trending diseases (most searched)
     */
    public List<Disease> getPopularDiseases(int limit) {
        // For now, return first diseases as "popular"
        // In real app, you'd track search analytics
        return getAllDiseases(0, limit);
    }

    /**
     * Get related diseases based on symptoms similarity
     */
    public List<Disease> getRelatedDiseases(int diseaseId, int limit) {
        Disease targetDisease = diseaseDAO.getDiseaseById(diseaseId);
        if (targetDisease == null) {
            return new ArrayList<>();
        }

        List<Disease> allDiseases = diseaseDAO.getAllDiseases();
        List<DiseaseMatch> matches = new ArrayList<>();

        String targetSymptoms = targetDisease.getSymptoms().toLowerCase();
        String[] targetKeywords = targetSymptoms.split("[,\\s]+");

        for (Disease disease : allDiseases) {
            if (disease.getId() != diseaseId) {
                int similarity = calculateSimilarity(targetKeywords, disease.getSymptoms().toLowerCase());
                if (similarity > 0) {
                    matches.add(new DiseaseMatch(disease, similarity));
                }
            }
        }

        // Sort by similarity and return top matches
        matches.sort((a, b) -> Integer.compare(b.similarity, a.similarity));

        List<Disease> results = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, matches.size()); i++) {
            results.add(matches.get(i).disease);
        }

        return results;
    }

    // ========== HELPER METHODS ==========

    private boolean containsDisease(List<Disease> diseases, int diseaseId) {
        for (Disease disease : diseases) {
            if (disease.getId() == diseaseId) {
                return true;
            }
        }
        return false;
    }

    private List<Disease> rankByRelevance(List<Disease> diseases, String searchTerms) {
        List<DiseaseMatch> matches = new ArrayList<>();
        String[] keywords = searchTerms.toLowerCase().split("[,\\s]+");

        for (Disease disease : diseases) {
            int relevance = calculateRelevance(disease, keywords);
            matches.add(new DiseaseMatch(disease, relevance));
        }

        // Sort by relevance score (descending)
        matches.sort((a, b) -> Integer.compare(b.similarity, a.similarity));

        List<Disease> results = new ArrayList<>();
        for (DiseaseMatch match : matches) {
            results.add(match.disease);
        }

        return results;
    }

    private int calculateRelevance(Disease disease, String[] keywords) {
        int score = 0;
        String symptoms = disease.getSymptoms().toLowerCase();
        String name = disease.getName().toLowerCase();

        for (String keyword : keywords) {
            // Higher score for name matches
            if (name.contains(keyword)) {
                score += 10;
            }
            // Medium score for symptom matches
            if (symptoms.contains(keyword)) {
                score += 5;
            }
        }

        return score;
    }

    private int calculateSimilarity(String[] targetKeywords, String compareText) {
        int matches = 0;
        for (String keyword : targetKeywords) {
            if (keyword.length() > 2 && compareText.contains(keyword)) {
                matches++;
            }
        }
        return matches;
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    // ========== INNER CLASSES ==========

    private static class DiseaseMatch {
        Disease disease;
        int similarity;

        DiseaseMatch(Disease disease, int similarity) {
            this.disease = disease;
            this.similarity = similarity;
        }
    }

    // ========== CALLBACK INTERFACES ==========

    public interface OnSearchCompleteListener {
        void onSuccess(List<Disease> diseases);
        void onError(String error);
    }

    public interface OnDiseaseDetailListener {
        void onSuccess(Disease disease);
        void onError(String error);
    }

    // ========== ASYNC SEARCH METHODS ==========

    /**
     * Async search by symptoms with callback
     */
    public void searchBySymptomsAsync(String symptoms, OnSearchCompleteListener listener) {
        new Thread(() -> {
            try {
                List<Disease> results = searchBySymptoms(symptoms);
                // Post back to main thread
                listener.onSuccess(results);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    /**
     * Async get disease details with callback
     */
    public void getDiseaseDetailsAsync(int diseaseId, OnDiseaseDetailListener listener) {
        new Thread(() -> {
            try {
                Disease disease = getDiseaseDetails(diseaseId);
                if (disease != null) {
                    listener.onSuccess(disease);
                } else {
                    listener.onError("Disease not found");
                }
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }
}