package de.mwvb.blockpuzzle.data;

import java.util.zip.CRC32;

import de.mwvb.blockpuzzle.Features;
import de.mwvb.blockpuzzle.GameState;
import de.mwvb.blockpuzzle.cluster.Cluster;
import de.mwvb.blockpuzzle.gamedefinition.GameDefinition;
import de.mwvb.blockpuzzle.persistence.IPersistence;
import de.mwvb.blockpuzzle.planet.IPlanet;

/**
 * Multi player game state exchange
 *
 * The game state isn't exchanged via Internet. A data string is created that have to be exchanged manually using other software (e.g. WhatsApp).
 */
public class DataService {
    public static final String VERSION = "1";

    public String get() {
        Cluster cluster = GameState.INSTANCE.getCluster();
        String quadrant = cluster.getQuadrant(GameState.INSTANCE.getPlanet().getX(), GameState.INSTANCE.getPlanet().getY());
        String ret = "BP" + VERSION + "/C" + cluster.getNumber() + ("ß".equals(quadrant) ? "b" : quadrant);
        IPersistence persistence = GameState.INSTANCE.getPersistence();
        for (IPlanet p : cluster.getPlanets()) {
            if (!p.isVisibleOnMap() || !p.isOwner()) {
                continue;
            }
            String q = cluster.getQuadrant(p.getX(), p.getY());
            if (!q.equals(quadrant)) {
                continue;
            }
            for (int gi = 0; gi < p.getGameDefinitions().size(); gi++) {
                persistence.setGameID(p, gi);
                int score = persistence.loadScore();
                int moves = persistence.loadMoves();
                if (moves > 0) {
                    ret += "/" + Integer.toHexString(p.getNumber()).toUpperCase() + "/" + gi + "/" + Integer.toHexString(score) + "/" + Integer.toHexString(moves);
                }
            }
        }
        ret = ret + "/" + code6(ret) + "//" + GameState.INSTANCE.getPlayername().replace(" ", "_");
        String k = "";
        int kl = 70;
        while (ret.length() > kl) {
            k += ret.substring(0, kl) + "\n";
            ret = ret.substring(kl);
        }
        return k + ret;
    }

    public String put(String data) {
        if (data != null && data.equals(get())) {
            return "Makes no sense to paste the copied data";
        } else if (data == null || !data.startsWith("BP")) {
            return "Unknown data format";
        } else if (!data.startsWith("BP" + VERSION + "/")) {
            return "Data is not compatible. Wrong version.";
        }
        String rhead = "BP" + VERSION + "/C" + GameState.INSTANCE.getCluster().getNumber();
        int headLength = rhead.length() + "a/".length();
        if (!data.startsWith(rhead + "a/") && !data.startsWith(rhead + "b/") && !data.startsWith(rhead + "c/") && !data.startsWith(rhead + "d/")) {
            return "Not supported star cluster."; // or quadrant
        }
        data = data.replace("\n", "");
        int loo = data.lastIndexOf("//");
        String name = "";
        if (loo >= 0) {
            name = data.substring(loo + "//".length()).replace("_", " ");
            data = data.substring(0, loo);
        }
        int lo = data.lastIndexOf("/");
        String code = data.substring(lo + 1);
        data = data.substring(0, lo);
        if (!Features.developerMode && !code6(data).equals(code)) {
            return "Checksum mismatch!";
        }
        data = data.substring(headLength);
        String[] w = data.split("/");
        if (w.length % 4 != 0) {
            return "Wrong planet data.";
        }
        for (int i = 0; i < w.length; i += 4) {
            parse(w[i], w[i + 1], w[i + 2], w[i + 3], name);
        }
        return null;
    }

    private void parse(String p0, String gi0, String s, String m, String name) {
        IPersistence persistence = GameState.INSTANCE.getPersistence();
        Cluster cluster = GameState.INSTANCE.getCluster();
        int pl = Integer.parseInt(p0, 16);
        int gi = Integer.parseInt(gi0);
        int otherScore = Integer.parseInt(s, 16);
        int otherMoves = Integer.parseInt(m, 16);
        for (int i = 0; i < cluster.getPlanets().size(); i++) {
            IPlanet p = cluster.getPlanets().get(i);
            if (p.getNumber() == pl) {
                persistence.setGameID(p, gi);
                int meineScore = persistence.loadScore();
                int meineMoves = persistence.loadMoves();
                GameDefinition gd = p.getGameDefinitions().get(gi);
                if (gd.isLiberated(otherScore, otherMoves, meineScore, meineMoves)) {
                    setOwner(p, gi, otherScore, otherMoves, name);
                }
                return;
            }
        }
        // PLanet nicht gefunden
    }

    private void setOwner(IPlanet p, int gi, int score, int moves, String name) {
        System.out.println("NEW OWNER for planet #" + p.getNumber() + ": gi=" + gi + ", score: " + score + ", moves: " + moves + ", name: " + name);
        IPersistence persistence = GameState.INSTANCE.getPersistence();
        persistence.setGameID(p, gi);
        persistence.saveOwner(score, moves, name);
    }

    private static String code6(String str) {
        CRC32 crc = new CRC32();
        crc.update(str.getBytes());
        String ret = "000000" + Integer.toString((int) crc.getValue(), 36).toLowerCase().replace("-", "");
        return ret.substring(ret.length() - 6);
    }
}
