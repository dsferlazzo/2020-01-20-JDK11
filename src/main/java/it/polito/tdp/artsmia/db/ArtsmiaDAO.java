package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Collegamento;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * ritorna la lista di tutti i ruoli presenti nel database
	 * @return
	 */
	public List<String> getRoles(){
		String sql = "SELECT DISTINCT(role) "
				+ "FROM authorship "
				+ "ORDER BY 1";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getString("role"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * dato un ruolo, ritorna la lista di tutti gli artisti per quel ruolo
	 * @param role
	 * @return
	 */
	public List<Artist> getArtistsByRole(String role){
		String sql = "SELECT DISTINCT a.* "
				+ "FROM artists a , authorship aut "
				+ "WHERE aut.role=? AND aut.artist_id=a.artist_id";
		List<Artist> result = new ArrayList<Artist>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(new Artist(res.getInt("artist_id"), res.getString("name")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * dato un ruolo, ritorna tutti gli oggetti art1, art2, weight, associati a quel ruolo
	 * @param role
	 * @return
	 */
	public List<Collegamento> getCollegamenti(String role){
		String sql = "WITH q1 "
				+ "AS "
				+ "	( "
				+ "		SELECT DISTINCT a.* "
				+ "		FROM artists a , authorship aut "
				+ "		WHERE aut.role=? AND aut.artist_id=a.artist_id "
				+ "	) "
				+ "SELECT a1.artist_id AS id1, a1.name AS n1, a2.artist_id AS id2, a2.name AS n2, COUNT(*) AS n "
				+ "FROM q1 a1, q1 a2, authorship aut1, authorship aut2, exhibition_objects eb1, exhibition_objects eb2 "
				+ "WHERE a1.artist_id = aut1.artist_id AND a2.artist_id = aut2.artist_id AND "
				+ "	aut1.object_id = eb1.object_id AND aut2.object_id = eb2.object_id AND eb1.exhibition_id = eb2.exhibition_id "
				+ "	AND a1.artist_id>a2.artist_id "
				+ "GROUP BY a1.artist_id, a2.artist_id, a1.name, a2.name";
		List<Collegamento> result = new ArrayList<Collegamento>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Artist a1 = new Artist(res.getInt("id1"), res.getString("n1"));
				Artist a2 = new Artist(res.getInt("id2"), res.getString("n2"));
				result.add(new Collegamento(a1, a2, res.getInt("n")));
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
