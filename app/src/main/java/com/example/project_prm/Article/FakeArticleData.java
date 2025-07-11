package com.example.project_prm.Article;

import java.util.ArrayList;
import java.util.List;

public class FakeArticleData {

    public static List<Article> getSampleArticles() {
        List<Article> articles = new ArrayList<>();

        articles.add(new Article(1,
                "COVID-19 Was a Top Cause of Death in 2020 and 2021, Even For Younger People",
                "COVID-19 was one of the leading causes of death in the United States during much of the pandemic, even for younger age groups, according to a new analysis. The results, which were published July 5 in JAMA Internal Medicine...",
                "covid_image", "Covid-19", "Dec 22, 2022", false, true));

        articles.add(new Article(2,
                "Study Finds Being 'Hangry' is a Real Thing",
                "New research confirms what many people have long suspected: being hungry can make you angry. The study published in PLOS ONE found that hunger can indeed affect our emotions and behavior...",
                "hangry_image", "Health", "Dec 22, 2022", false, false));

        articles.add(new Article(3,
                "Why Childhood Obesity Rates Are Rising and What We Can Do",
                "Childhood obesity has become a growing concern worldwide. Recent studies show alarming trends in obesity rates among children and adolescents...",
                "obesity_image", "Lifestyle", "Dec 21, 2022", false, true));

        articles.add(new Article(4,
                "Depression Treatment: How Genetic Testing Can Help Find the Right Medication",
                "Genetic testing is revolutionizing how doctors approach depression treatment. By analyzing a patient's genetic makeup, healthcare providers can better predict...",
                "depression_image", "Medical", "Dec 20, 2022", false, true));

        articles.add(new Article(5,
                "Drinking Alone as a Teen May Foreshadow Future Alcohol Problems",
                "A new study suggests that teenagers who drink alcohol alone may be at higher risk for developing alcohol use disorders later in life...",
                "alcohol_image", "Health", "Dec 20, 2022", false, true));

        articles.add(new Article(6,
                "Colorectal Cancer: Why Adults in Their 40s and 50s Need to Get Screened",
                "Colorectal cancer rates are rising among younger adults, making early screening more important than ever. Medical experts now recommend...",
                "cancer_image", "Medical", "Dec 19, 2022", false, false));

        articles.add(new Article(7,
                "Adding Salt to Your Food May Increase Risk of Premature Death",
                "A large-scale study has found that people who regularly add salt to their food at the table may have a higher risk of premature death...",
                "salt_image", "Health", "Dec 18, 2022", false, true));

        articles.add(new Article(8,
                "COVID-19 Reinfections May Increase the Risk of Serious Health Problems",
                "Multiple COVID-19 infections may compound health risks, according to new research. The study suggests that reinfections are not harmless...",
                "reinfection_image", "Covid-19", "Dec 17, 2022", false, true));

        return articles;
    }
}

