package edu.uco.map2016.mediaplayer.api;

import android.net.Uri;

import java.util.Vector;

public class SearchInterface {
    private Vector<MediaFile> listToSearch = new Vector<MediaFile>();
    private Vector<Integer> listRelevance = new Vector<Integer>();

    protected Vector<MediaFile> listResult = new Vector<MediaFile>();

    public SearchInterface() {}

    public Vector<MediaFile> getListResult() {
        return listResult;
    }

    private void retrieveListToSearch() {
        listToSearch.add(new MediaFile("This Is How We Do", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:24","Katy Perry", "PRISM (Deluxe)", "2013"));
        listToSearch.add(new MediaFile("My Songs Know What You Did In The Dark (Light Em Up)", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:07","Fall Out Boy", "Save Rock And Roll", "2013"));
        listToSearch.add(new MediaFile("Counting Stars", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "4:17","OneRepublic", "Native (Deluxe)", "2013"));
        listToSearch.add(new MediaFile("Still Into You", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:36","Paramore", "Paramore", "2013"));
        listToSearch.add(new MediaFile("Lights - Single Version", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:31","Ellie Goulding", "Bright Lights", "2010"));
        listToSearch.add(new MediaFile("Tongue Tied", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:38","Grouplove", "Never Trust A Happy Song", "2011"));
        listToSearch.add(new MediaFile("On Top Of The World", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:10","Imagine Dragons", "Night Visions (Deluxe)", "2012"));
        listToSearch.add(new MediaFile("Alive", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "4:51","Krewella", "Play Hard EP", "2012"));
        listToSearch.add(new MediaFile("Stay The Night - featuring Hayley Williams of Paramore", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:57","Zedd", "Stay The Night - featuring Hayley Williams of Paramore", "2013"));
        listToSearch.add(new MediaFile("What You Know", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:10","Two Door Cinema Club", "Tourist History", "2010"));
        listToSearch.add(new MediaFile("I Need Your Love", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:55","Calvin Harris", "18 Months", "2012"));
        listToSearch.add(new MediaFile("Ten Feet Tall", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:53","Afrojack", "Ten Feet Tall", "2014"));
        listToSearch.add(new MediaFile("How To Be A Heartbreaker", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:41","Marina and the Diamonds", "How To Be A Heartbreaker", "2012"));
        listToSearch.add(new MediaFile("Tonight Is The Night", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:10","Outasight", "Tonight Is The Night", "2011"));
        listToSearch.add(new MediaFile("It Was Always You", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:58","Maroon 5", "It Was Always You", "2014"));
        listToSearch.add(new MediaFile("Closer", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:29","Tegan and Sara", "Heartthrob", "2013"));
        listToSearch.add(new MediaFile("Royals", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:10","Lorde", "Pure Heroine", "2013"));
        listToSearch.add(new MediaFile("Roar", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:44","Katy Perry", "PRISM (Deluxe)", "2013"));
        listToSearch.add(new MediaFile("Clarity", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "4:31","Zedd", "Clarity", "2012"));
        listToSearch.add(new MediaFile("Sail", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "4:19","AWOLNATION", "Megalithic Symphony", "2011"));
        listToSearch.add(new MediaFile("Drive", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "4:15","Miley Cyrus", "Bangerz (Deluxe Version)", "2013"));

        listToSearch.add(new MediaFile("NES CLASSIC EDITION Unboxing Demo! | Lamarr Wilson", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "14:26","Lamarr Wilson", "", "2016"));
        listToSearch.add(new MediaFile("Unboxing and trying the NES Classic Edition", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "19:10","CNET", "", "2016"));
        listToSearch.add(new MediaFile("Gordon Ramsay Cannot Believe Italian Restaurant Has A Drive-Thru!", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:07","Kitchen Nightmares", "", "2016"));
        listToSearch.add(new MediaFile("It's Wedding Day for Jeremy & Jinger!", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "0:31","Duggar Family", "", "2016"));
        listToSearch.add(new MediaFile("Hilary Clinton/Donald Trump Cold Open - SNL", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "9:24","Saturday Night Live", "", "2016"));
        listToSearch.add(new MediaFile("The Future.", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "0:16","mike dee", "", "2016"));
        listToSearch.add(new MediaFile("Full Speech: Donald Trump Rally in Reno, NV 11/5/16", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "2:22:00","Right Side Broadcasting", "", "2016"));
        listToSearch.add(new MediaFile("The Philosophy of THE PURGE (with Rick & Morty!) - Wisecrack Edition", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "11:40","Wisecrack", "", "2016"));
        listToSearch.add(new MediaFile("Future - Used to This ft. Drake", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:50","FutureVEVO", "", "2016"));
        listToSearch.add(new MediaFile("The LEGO Batman Movie - Trailer #4", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "2:31","Warner Bros Pictures", "", "2016"));
        listToSearch.add(new MediaFile("The Da Vinci Code", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:56","Dan Brown", "", "2002"));
        listToSearch.add(new MediaFile("1984", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:56","George Orwell ", "", "2002"));
        listToSearch.add(new MediaFile("Pride and Prejudice", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:56","Jane Austen", "", "2002"));
        listToSearch.add(new MediaFile("To Kill a Mockingbird", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:56","Harper Lee", "", "2002"));
        listToSearch.add(new MediaFile("The Catcher in the Rye", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:56","J. D. Salinger", "", "2002"));
        listToSearch.add(new MediaFile("The Great Gatsby", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:56","F. Scott Fitzgerald", "", "2002"));
        listToSearch.add(new MediaFile("Twilight", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:56","Stephenie Meyer", "", "YEAR"));
        listToSearch.add(new MediaFile("The Hunger Games", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:56","Suzanne Collins", "", "2002"));
        listToSearch.add(new MediaFile("The Kite Runner", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:56","Khaled Hosseini", "", "2002"));
        listToSearch.add(new MediaFile("Animal Farm", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:56","George Orwell", "", "YEAR"));
        listToSearch.add(new MediaFile("The Lord of the Rings", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:56","J. R. R. Tolkien", "", "2002"));

        /*
        listToSearch.add(new MediaFile("This Is How We Do", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:24","Katy Perry", "PRISM (Deluxe)", "2013"));
        listToSearch.add(new MediaFile("My Songs Know What You Did In The Dark (Light Em Up)", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:07","Fall Out Boy", "Save Rock And Roll", "2013"));
        listToSearch.add(new MediaFile("Counting Stars", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:17","OneRepublic", "Native (Deluxe)", "2013"));
        listToSearch.add(new MediaFile("Still Into You", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:36","Paramore", "Paramore", "2013"));
        listToSearch.add(new MediaFile("Lights - Single Version", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:31","Ellie Goulding", "Bright Lights", "2010"));
        listToSearch.add(new MediaFile("Tongue Tied", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:38","Grouplove", "Never Trust A Happy Song", "2011"));
        listToSearch.add(new MediaFile("On Top Of The World", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:10","Imagine Dragons", "Night Visions (Deluxe)", "2012"));
        listToSearch.add(new MediaFile("Alive", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:51","Krewella", "Play Hard EP", "2012"));
        listToSearch.add(new MediaFile("Stay The Night - featuring Hayley Williams of Paramore", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:57","Zedd", "Stay The Night - featuring Hayley Williams of Paramore", "2013"));
        listToSearch.add(new MediaFile("What You Know", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:10","Two Door Cinema Club", "Tourist History", "2010"));
        listToSearch.add(new MediaFile("I Need Your Love", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:55","Calvin Harris", "18 Months", "2012"));
        listToSearch.add(new MediaFile("Ten Feet Tall", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:53","Afrojack", "Ten Feet Tall", "2014"));
        listToSearch.add(new MediaFile("How To Be A Heartbreaker", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:41","Marina and the Diamonds", "How To Be A Heartbreaker", "2012"));
        listToSearch.add(new MediaFile("Tonight Is The Night", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:10","Outasight", "Tonight Is The Night", "2011"));
        listToSearch.add(new MediaFile("It Was Always You", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:58","Maroon 5", "It Was Always You", "2014"));
        listToSearch.add(new MediaFile("Closer", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:29","Tegan and Sara", "Heartthrob", "2013"));
        listToSearch.add(new MediaFile("Royals", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:10","Lorde", "Pure Heroine", "2013"));
        listToSearch.add(new MediaFile("Roar", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "3:44","Katy Perry", "PRISM (Deluxe)", "2013"));
        listToSearch.add(new MediaFile("Clarity", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:31","Zedd", "Clarity", "2012"));
        listToSearch.add(new MediaFile("Sail", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:19","AWOLNATION", "Megalithic Symphony", "2011"));
        listToSearch.add(new MediaFile("Drive", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_VIDEO, "4:15","Miley Cyrus", "Bangerz (Deluxe Version)", "2013"));
        */
        /*
        listToSearch.add(new MediaFile("Alive", Uri.parse("http://www.example.com"),MediaFile.TYPE_AUDIO));
        listToSearch.add(new MediaFile("Helena Beat",Uri.parse("http://www.example.com"),MediaFile.TYPE_AUDIO));
        listToSearch.add(new MediaFile("The Mother We Share",Uri.parse("http://www.example.com"),MediaFile.TYPE_AUDIO));
        listToSearch.add(new MediaFile("Applause",Uri.parse("http://www.example.com"),MediaFile.TYPE_AUDIO));
        listToSearch.add(new MediaFile("Take Me Home (feat. Bebe Rexha)",Uri.parse("http://www.example.com"),MediaFile.TYPE_AUDIO));
        listToSearch.add(new MediaFile("The new MacBook Pro.",Uri.parse("http://www.example.com"),MediaFile.TYPE_VIDEO));
        listToSearch.add(new MediaFile("Moog System 55 Modular Synth - Sweetwater Exclusive Preview",Uri.parse("http://www.example.com"),MediaFile.TYPE_VIDEO));
        listToSearch.add(new MediaFile("Cool Things To Do With The Korg",Uri.parse("http://www.example.com"),MediaFile.TYPE_VIDEO));
        listToSearch.add(new MediaFile("Nerd Out: DJ Stands - UDG Creator vs Crane Pro",Uri.parse("http://www.example.com"),MediaFile.TYPE_VIDEO));
        listToSearch.add(new MediaFile("This is the most used sound in Rap, Hip Hop, and Trap (Tutorial)",Uri.parse("http://www.example.com"),MediaFile.TYPE_VIDEO));
        */
    }

    public void search(String searchCriteria) {
        retrieveListToSearch();

        for (int cntr0 = 0; cntr0 < listToSearch.size(); cntr0++) { // For each element in list to search,
            listRelevance.add(0);

            // search name

            for (int cntr = 0; cntr <= searchCriteria.length(); cntr++) { // search for matching string from cntr
                for (int cntr2 = cntr; cntr2 <= searchCriteria.length(); cntr2++) { // to cntr2 from search criteria.
                    if (listToSearch.get(cntr0).getName().toLowerCase().contains(searchCriteria.substring(cntr, cntr2).toLowerCase())) {
                        /*if (cntr2-cntr > 0) {
                            String temp = listToSearch.get(cntr0).getName().toLowerCase();
                            while (temp.contains(searchCriteria.substring(cntr, cntr2).toLowerCase())) {
                                listRelevance.set(cntr0, listRelevance.get(cntr0) + 1);
                                temp.replaceFirst(searchCriteria.substring(cntr, cntr2).toLowerCase(),"");
                            }
                        }*/
                        if (cntr2-cntr > 0) {
                            listRelevance.set(cntr0, listRelevance.get(cntr0)+1);
                        }
                    }
                }
            }

            // search type

            for (int cntr = 0; cntr <= searchCriteria.length(); cntr++) { // search for matching string from cntr
                for (int cntr2 = cntr; cntr2 <= searchCriteria.length(); cntr2++) { // to cntr2 from search criteria.
                    if ((listToSearch.get(cntr0).getType()==MediaFile.TYPE_AUDIO?"audio":"video").contains(searchCriteria.substring(cntr, cntr2).toLowerCase())) {
                        /*if (cntr2-cntr > 0) {
                            String temp = listToSearch.get(cntr0).getName().toLowerCase();
                            while (temp.contains(searchCriteria.substring(cntr, cntr2).toLowerCase())) {
                                listRelevance.set(cntr0, listRelevance.get(cntr0) + 1);
                                temp.replaceFirst(searchCriteria.substring(cntr, cntr2).toLowerCase(),"");
                            }
                        }*/
                        if (cntr2-cntr > 0) {
                            listRelevance.set(cntr0, listRelevance.get(cntr0)+1);
                        }
                    }
                }
            }

            // search length

            for (int cntr = 0; cntr <= searchCriteria.length(); cntr++) { // search for matching string from cntr
                for (int cntr2 = cntr; cntr2 <= searchCriteria.length(); cntr2++) { // to cntr2 from search criteria.
                    if (listToSearch.get(cntr0).getLengthOfFile().toLowerCase().contains(searchCriteria.substring(cntr, cntr2).toLowerCase())) {
                        /*if (cntr2-cntr > 0) {
                            String temp = listToSearch.get(cntr0).getName().toLowerCase();
                            while (temp.contains(searchCriteria.substring(cntr, cntr2).toLowerCase())) {
                                listRelevance.set(cntr0, listRelevance.get(cntr0) + 1);
                                temp.replaceFirst(searchCriteria.substring(cntr, cntr2).toLowerCase(),"");
                            }
                        }*/
                        if (cntr2-cntr > 0) {
                            listRelevance.set(cntr0, listRelevance.get(cntr0)+1);
                        }
                    }
                }
            }

            // search artist

            for (int cntr = 0; cntr <= searchCriteria.length(); cntr++) { // search for matching string from cntr
                for (int cntr2 = cntr; cntr2 <= searchCriteria.length(); cntr2++) { // to cntr2 from search criteria.
                    if (listToSearch.get(cntr0).getArtist().toLowerCase().contains(searchCriteria.substring(cntr, cntr2).toLowerCase())) {
                        /*if (cntr2-cntr > 0) {
                            String temp = listToSearch.get(cntr0).getName().toLowerCase();
                            while (temp.contains(searchCriteria.substring(cntr, cntr2).toLowerCase())) {
                                listRelevance.set(cntr0, listRelevance.get(cntr0) + 1);
                                temp.replaceFirst(searchCriteria.substring(cntr, cntr2).toLowerCase(),"");
                            }
                        }*/
                        if (cntr2-cntr > 0) {
                            listRelevance.set(cntr0, listRelevance.get(cntr0)+1);
                        }
                    }
                }
            }

            // search album

            for (int cntr = 0; cntr <= searchCriteria.length(); cntr++) { // search for matching string from cntr
                for (int cntr2 = cntr; cntr2 <= searchCriteria.length(); cntr2++) { // to cntr2 from search criteria.
                    if (listToSearch.get(cntr0).getAlbum().toLowerCase().contains(searchCriteria.substring(cntr, cntr2).toLowerCase())) {
                        /*if (cntr2-cntr > 0) {
                            String temp = listToSearch.get(cntr0).getName().toLowerCase();
                            while (temp.contains(searchCriteria.substring(cntr, cntr2).toLowerCase())) {
                                listRelevance.set(cntr0, listRelevance.get(cntr0) + 1);
                                temp.replaceFirst(searchCriteria.substring(cntr, cntr2).toLowerCase(),"");
                            }
                        }*/
                        if (cntr2-cntr > 0) {
                            listRelevance.set(cntr0, listRelevance.get(cntr0)+1);
                        }
                    }
                }
            }

            // search year published

            for (int cntr = 0; cntr <= searchCriteria.length(); cntr++) { // search for matching string from cntr
                for (int cntr2 = cntr; cntr2 <= searchCriteria.length(); cntr2++) { // to cntr2 from search criteria.
                    if (listToSearch.get(cntr0).getYearPublished().toLowerCase().contains(searchCriteria.substring(cntr, cntr2).toLowerCase())) {
                        /*if (cntr2-cntr > 0) {
                            String temp = listToSearch.get(cntr0).getName().toLowerCase();
                            while (temp.contains(searchCriteria.substring(cntr, cntr2).toLowerCase())) {
                                listRelevance.set(cntr0, listRelevance.get(cntr0) + 1);
                                temp.replaceFirst(searchCriteria.substring(cntr, cntr2).toLowerCase(),"");
                            }
                        }*/
                        if (cntr2-cntr > 0) {
                            listRelevance.set(cntr0, listRelevance.get(cntr0)+1);
                        }
                    }
                }
            }

        }
        int timesToRepeatLoop = listToSearch.size();
        for (int cntr = 0; cntr < timesToRepeatLoop; cntr++) { // for every elemennt at the start of the loop
            int maxIndex = 0; // most relevant index
            for (int cntr2 = 0; cntr2 < listToSearch.size(); cntr2++) { // find the most relevant, add to list result, and remove element
                if (listRelevance.get(cntr2) > listRelevance.get(maxIndex)) {
                    maxIndex = cntr2;
                }
            }
            listResult.add(listToSearch.get(maxIndex));
            //listResult.add(new MediaFile(Integer.toString(listRelevance.get(maxIndex)),"",0));
            listToSearch.remove(maxIndex);
            listRelevance.remove(maxIndex);
        }
        //listResult.add(new MediaFile(searchCriteria,"", 0));
    }
}
