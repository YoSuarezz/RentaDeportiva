package co.edu.uco.unidaddeportivaelbernabeu.business.usecase.concrete;

import co.edu.uco.unidaddeportivaelbernabeu.business.usecase.UseCaseWithoutReturn;
import co.edu.uco.unidaddeportivaelbernabeu.crosscutting.exceptions.custom.BusinessUDElBernabeuException;
import co.edu.uco.unidaddeportivaelbernabeu.crosscutting.exceptions.messagecatalog.MessageCatalogStrategy;
import co.edu.uco.unidaddeportivaelbernabeu.crosscutting.exceptions.messagecatalog.data.CodigoMensaje;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.espaciosdeportivos.TipoEspacioDeportivoDAO;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.factory.DAOFactory;
import co.edu.uco.unidaddeportivaelbernabeu.entity.TipoEspacioDeportivoEntity;

public class EliminarTipoEspacioDeportivoImpl implements UseCaseWithoutReturn<Integer> {

    private final DAOFactory factory;

    public EliminarTipoEspacioDeportivoImpl(DAOFactory factory) {
        this.factory = factory;
    }

    @Override
    public void ejecutar(Integer id) {
        validarDatos(id);
        TipoEspacioDeportivoDAO dao = factory.getTipoEspacioDeportivoDAO();
        TipoEspacioDeportivoEntity entidad = new TipoEspacioDeportivoEntity(id);

        // Verificar si el tipo de espacio deportivo existe
        if (dao.consultar(entidad).isEmpty()) {
            var mensajeUsuario = MessageCatalogStrategy.getContenidoMensaje(CodigoMensaje.M00075);
            throw new BusinessUDElBernabeuException(mensajeUsuario);
        }

        // Si existe, proceder con la eliminación
        dao.eliminar(id);
    }

    private void validarDatos(Integer id) {
        if (id == null ) {
            var mensajeUsuriao = MessageCatalogStrategy.getContenidoMensaje(CodigoMensaje.M00076);
            throw new BusinessUDElBernabeuException(mensajeUsuriao);
        }
        if (id <= 0) {
            var mensajeUsuario = MessageCatalogStrategy.getContenidoMensaje(CodigoMensaje.M00077);
            throw new BusinessUDElBernabeuException(mensajeUsuario);
        }
    }
}
