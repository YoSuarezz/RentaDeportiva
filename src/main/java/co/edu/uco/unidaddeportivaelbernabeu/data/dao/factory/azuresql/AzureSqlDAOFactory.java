package co.edu.uco.unidaddeportivaelbernabeu.data.dao.factory.azuresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import co.edu.uco.unidaddeportivaelbernabeu.crosscutting.exceptions.custom.DataUDElBernabeuException;
import co.edu.uco.unidaddeportivaelbernabeu.crosscutting.exceptions.messagecatalog.MessageCatalogStrategy;
import co.edu.uco.unidaddeportivaelbernabeu.crosscutting.exceptions.messagecatalog.data.CodigoMensaje;
import co.edu.uco.unidaddeportivaelbernabeu.crosscutting.helpers.SQLHelper;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.DeporteDAO;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.TipoEspacioDeportivoDAO;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.UnidadDeportivaDAO;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.factory.DAOFactory;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.factory.enums.Factory;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.sql.azuresql.DeporteAzureSqlDAO;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.sql.azuresql.TipoEspacioDeportivoAzureSqlDAO;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.sql.azuresql.UnidadDeportivaAzureSqlDAO;
import co.edu.uco.unidaddeportivaelbernabeu.entity.DeporteEntity;
import co.edu.uco.unidaddeportivaelbernabeu.entity.TipoEspacioDeportivoEntity;
import co.edu.uco.unidaddeportivaelbernabeu.entity.UnidadDeportivaEntity;

public final class AzureSqlDAOFactory extends DAOFactory {

	private Connection connection;

	public AzureSqlDAOFactory() {
		obtenerConexion();
	}

	@Override
	protected void obtenerConexion() {
		final String connectionUrl = "jdbc:sqlserver://unidaddeportivaelbernabeu-server.database.windows.net:1433;database=UnidadDeportivaElBernabeu;user=Administrador@unidaddeportivaelbernabeu-server;password=ProyectoDoo*;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
		try {
			connection = DriverManager.getConnection(connectionUrl);
		} catch (final SQLException excepcion) {
			var mensajeUsuario = MessageCatalogStrategy.getContenidoMensaje(CodigoMensaje.M00002);
			var mensajeTecnico = MessageCatalogStrategy.getContenidoMensaje(CodigoMensaje.M00044);

			throw new DataUDElBernabeuException(mensajeTecnico, mensajeUsuario, excepcion);
		} catch (final Exception excepcion) {
			var mensajeUsuario = MessageCatalogStrategy.getContenidoMensaje(CodigoMensaje.M00002);
			var mensajeTecnico = MessageCatalogStrategy.getContenidoMensaje(CodigoMensaje.M00045);

			throw new DataUDElBernabeuException(mensajeTecnico, mensajeUsuario, excepcion);
		}
	}

	@Override
	public void iniciarTransaccion() {
		SQLHelper.initTransaction(connection);
	}

	@Override
	public void confirmarTransaccion() {
		SQLHelper.commit(connection);
	}

	@Override
	public void cancelarTransaccion() {
		SQLHelper.rollback(connection);
	}

	@Override
	public void cerrarConexion() {
		SQLHelper.close(connection);
	}

	@Override
	public DeporteDAO getDeporteDAO() {
		return new DeporteAzureSqlDAO(connection);
	}

	@Override
	public UnidadDeportivaDAO getUnidadDeportivaDAO() {
		return new UnidadDeportivaAzureSqlDAO(connection);
	}

	@Override
	public TipoEspacioDeportivoDAO getTipoEspacioDeportivoDAO() {
		return new TipoEspacioDeportivoAzureSqlDAO(connection);
	}

	public static void main(String[] args) {
        DAOFactory factory = null;
        try {
            factory = DAOFactory.getFactory(Factory.AZURE_SQL);

            System.out.println("Iniciando transacción...");
            factory.iniciarTransaccion();
            System.out.println("Confirmando transacción...");
            factory.confirmarTransaccion();
            System.out.println("Cerrando conexión...");
            factory.cerrarConexion();
        } catch (final Exception excepcion) {
            excepcion.printStackTrace();
        }
    }
}