package com.innertrack.util;

import com.innertrack.model.EntreeJournal;
import com.innertrack.model.Habitude;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.List;

public class PdfExporter {

    public static void exportHabitudes(List<Habitude> habitudes, String path) throws Exception {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(path));

        document.open();

        document.add(new Paragraph("Liste des Habitudes"));
        document.add(new Paragraph("------------------------------------------------"));

        for (Habitude h : habitudes) {
            document.add(new Paragraph("Nom : " + h.getNomHabitude()));
            document.add(new Paragraph("Emotion : " + h.getEmotionDominantes()));
            document.add(new Paragraph("Energie : " + h.getNiveauEnergie()));
            document.add(new Paragraph("Stress : " + h.getNiveauStress()));
            document.add(new Paragraph("Sommeil : " + h.getQualiteSommeil()));
            document.add(new Paragraph("Date : " + h.getDateCreation()));
            document.add(new Paragraph(" "));
        }

        document.close();
    }

    public static void exportJournal(List<EntreeJournal> entrees, String path) throws Exception {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(path));

        document.open();

        document.add(new Paragraph("Journal Émotionnel"));
        document.add(new Paragraph("------------------------------------------------"));

        for (EntreeJournal e : entrees) {
            document.add(new Paragraph("Humeur : " + e.getHumeur()));
            document.add(new Paragraph("Note : " + e.getNoteTextuelle()));
            document.add(new Paragraph("Date : " + e.getDateSaisie()));
            document.add(new Paragraph(" "));
        }

        document.close();
    }
}