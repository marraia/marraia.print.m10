package plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import android.net.Uri;
import android.content.Intent;
import android.provider.MediaStore;

import static android.app.Activity.RESULT_OK;

import java.util.HashMap;
import java.util.Map;

//Elgin Plugins
import com.elgin.e1.Impressora.Termica;

import javax.security.auth.callback.Callback;


public class MainActivity extends CordovaPlugin {

    //Cordova/Java Params
    private Activity mActivity;
    private Context mContext;
    private CordovaWebView webView;

    //Intents CODES
    private static int REQ_CODE_SELECAOIMAGEM = 1234;

    //CallbacksOnActivityResult
    private CallbackContext selecionarImagemCallbackContext;

    //cutPaper boolean for resultActivity
    private boolean cutPaper = false;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.webView = webView;
        mActivity = cordova.getActivity();
        mContext = cordova.getActivity().getApplicationContext();

        //Initialize printer
        Termica.setContext(mActivity);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if(action.equals("AbreConexaoImpressora")){
            try{
                JSONObject params = args.getJSONObject(0);

                int tipo = params.getInt("tipo");
                String modelo = params.getString("modelo");
                String conexao = params.getString("conexao");
                int parametro = params.getInt("parametro");

                int result = Termica.AbreConexaoImpressora(tipo, modelo, conexao, parametro);

                callbackContext.success(result);
            }catch (Exception e){
                callbackContext.error("AbreConexaoImpressora error: " + e.toString());
            }
        }

        else if(action.equals("FechaConexaoImpressora")){
            try{
                int result = Termica.FechaConexaoImpressora();

                callbackContext.success(result);
            } catch (Exception e){
                callbackContext.error("FechaConexaoImpressora error: " + e.toString());
            }
        }

        else if(action.equals("AvancaPapel")){
            try{
                JSONObject params = args.getJSONObject(0);

                int linhas = params.getInt("linhas");

                int result = Termica.AvancaPapel(linhas);

                callbackContext.success(result);
            }catch (Exception e){
                callbackContext.error("AvancaPapel error: " + e.toString());
            }
        }

        else if(action.equals("Corte")){
            try{
                JSONObject params = args.getJSONObject(0);

                int avanco = params.getInt("avanco");

                int result = Termica.Corte(avanco);

                callbackContext.success(result);
            }catch (Exception e){
                callbackContext.error("Corte error: " + e.toString());
            }
        }

       else if(action.equals("ImpressaoTexto")){
           try{
                JSONObject params = args.getJSONObject(0);

                String dados = params.getString("dados");
                int posicao = params.getInt("posicao");
                int stilo = params.getInt("stilo");
                int tamanho = params.getInt("tamanho");

                int result = Termica.ImpressaoTexto(dados, posicao, stilo, tamanho);

                callbackContext.success(result);
           }catch (Exception e){
               callbackContext.error("ImpressaoTexto error: " + e.toString());
           }
           return true;
       }

        else if(action.equals("ImpressaoCodigoBarras")){
            try{
                JSONObject params = args.getJSONObject(0);

                int tipo = params.getInt("tipo");
                String dados = params.getString("dados");
                int altura = params.getInt("altura");
                int largura = params.getInt("largura");
                int HRI = params.getInt("HRI");

                int result = Termica.ImpressaoCodigoBarras(tipo, dados, altura, largura, HRI);

                callbackContext.success(result);
            }catch (Exception e){
                callbackContext.error("ImpressaoCodigoBarras error: " + e.toString());
            }
            return true;
        }

        else if (action.equals("DefinePosicao")){
            try{
                JSONObject params = args.getJSONObject(0);

                int posicao = params.getInt("posicao");

                int result = Termica.DefinePosicao(posicao);

                callbackContext.success(result);
            }catch (Exception e){
                callbackContext.error("DefinePosicao error: " + e.toString());
            }
            return true;
        }

        else if(action.equals("ImpressaoQRCode")){
            try{
                JSONObject params = args.getJSONObject(0);

                String dados = params.getString("dados");
                int tamanho = params.getInt("tamanho");
                int nivelCorrecao = params.getInt("nivelCorrecao");

                int result = Termica.ImpressaoQRCode(dados, tamanho, nivelCorrecao);

                callbackContext.success(result) ;
            }catch (Exception e){
                callbackContext.error("ImpressaoQRCode error: " + e.toString());
            }
            return true;
        }

        else if(action.equals("ImprimeXMLNFCe")){
            try{
                JSONObject params = args.getJSONObject(0);

                String dados = params.getString("dados");
                int indexcsc = params.getInt("indexcsc");
                String csc = params.getString("csc");
                int param = params.getInt("param");

                int result = Termica.ImprimeXMLNFCe(dados, indexcsc, csc, param);

                callbackContext.success(result);
            }catch (Exception e){
                callbackContext.error("ImprimeXMLNFCe error: " + e.toString()); 
            }
            return true;
        }

        else if(action.equals("ImprimeXMLSAT")){
            try{
                JSONObject params = args.getJSONObject(0);

                String dados = params.getString("dados");
                int param = params.getInt("param");

                int result = Termica.ImprimeXMLSAT(dados, param);

                callbackContext.success(result);

            }catch (Exception e){
                callbackContext.error("ImprimeXMLSAT error: " + e.toString());
            }
            return true;
        }

        else if(action.equals("AbreGavetaElgin")){
            try{
                int result = Termica.AbreGavetaElgin();

                callbackContext.success(result);
            } catch (Exception e){
                callbackContext.error("AbreGavetaElgin error: " + e.toString());
            }
        }

        else if(action.equals("StatusImpressora")){
            try{
                JSONObject params = args.getJSONObject(0);

                int param = params.getInt("param");

                int result = Termica.StatusImpressora(param);

                callbackContext.success(result);
            }catch (Exception e){
                callbackContext.error("StatusImpressora error: " + e.toString());
            }
            return true;
        }

        else if(action.equals("ImprimeImagem")){
            try{
                JSONObject params = args.getJSONObject(0);

                if(params.getBoolean("cutPaper")) this.cutPaper = true;
                else this.cutPaper = false;

                Intent intent = new Intent((Intent.ACTION_PICK));
                intent.setType("image/*");

                this.selecionarImagemCallbackContext = callbackContext;

                cordova.setActivityResultCallback(this);
                cordova.getActivity().startActivityForResult(intent, REQ_CODE_SELECAOIMAGEM);

            }catch (Exception e){
                callbackContext.error("ImprimirImagem error: " + e.toString());
            }
            return true;
        }

        else if(action.equals("ImprimePedido")){
            try{
                JSONObject params = args.getJSONObject(0);

                String dados = params.getString("dados");
                int posicao = params.getInt("posicao");
                int stilo = params.getInt("stilo");
                int tamanho = params.getInt("tamanho");
                String data = params.getString("data");
                String senha = params.getString("senha");

                int result = Termica.ImprimeImagem("base64=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");
                Termica.ImpressaoTexto("================================",posicao, stilo, 10);
                Termica.ImpressaoTexto(dados, posicao, stilo, tamanho);

                if(senha.length() != 0) {
                    Termica.ImpressaoTexto("================================",posicao, stilo, 10);
                    Termica.ImpressaoTexto(senha, posicao, 8, 10);
                }

                Termica.ImpressaoTexto("================================",posicao, stilo, 10);
                Termica.ImpressaoTexto(data, 1, stilo, 10);
                Termica.ImpressaoTexto("@eventflow v1.0", 1, 8, 10);
                Termica.AvancaPapel(5);
                Termica.Corte(10);

                callbackContext.success(result);
            }catch (Exception e){
                callbackContext.error("ImprimePedido error: " + e.toString()); 
            }
            return true;
        }

        else if(action.equals("ImprimePedidoConsolidado")){
            try{
                JSONObject params = args.getJSONObject(0);

                String dados = params.getString("dados");
                int posicao = params.getInt("posicao");
                int stilo = params.getInt("stilo");
                int tamanho = params.getInt("tamanho");
                String total = params.getString("total");
                String observacao = params.getString("observacao");
                String data = params.getString("data");
                String senha = params.getString("senha");
                String textoTotal = "TOTAL                 " + total;

                int result = Termica.ImprimeImagem("base64=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");
                Termica.ImpressaoTexto("================================",posicao, stilo, tamanho);
                Termica.ImpressaoTexto("# |       ITEM       | VALOR   ",posicao, 8, tamanho);
                Termica.ImpressaoTexto(dados, posicao, stilo, tamanho);
                Termica.ImpressaoTexto("================================",posicao, stilo, tamanho);
                Termica.ImpressaoTexto(textoTotal, 0, 8, tamanho);

                if(senha.length() != 0) {
                    Termica.ImpressaoTexto("================================",posicao, stilo, tamanho);
                    Termica.ImpressaoTexto(senha, posicao, 8, tamanho);
                }
                
                if(observacao.length() != 0) {
                    Termica.ImpressaoTexto("================================",posicao, stilo, tamanho);
                    Termica.ImpressaoTexto("         OBSERVAÇÕES            ",posicao, 8, tamanho);
                    Termica.ImpressaoTexto(observacao, posicao, stilo, tamanho);
                }

                Termica.ImpressaoTexto("================================",posicao, stilo, tamanho);
                Termica.ImpressaoTexto(data, 1, stilo, 10);
                Termica.ImpressaoTexto("@eventflow v1.0", 1, 8, 10);
                Termica.AvancaPapel(5);
                Termica.Corte(10);

                callbackContext.success(result);
            }catch (Exception e){
                callbackContext.error("ImprimePedidoConsolidado error: " + e.toString()); 
            }
            return true;
        }

        //could not find a method
        return false;
    }

    //Activity Result

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        JSONObject resultadoJson = new JSONObject();

        if(resultCode == RESULT_OK){
            if(requestCode == this.REQ_CODE_SELECAOIMAGEM){
                Uri returnUri = data.getData();

                Bitmap bitmapImage = null;

                try{
                   
                    bitmapImage = MediaStore.Images.Media.getBitmap(cordova.getActivity().getContentResolver(), returnUri);
                    
                    int result = Termica.ImprimeBitmap(bitmapImage);
                    Termica.AvancaPapel(10);
                    if(cutPaper) Termica.Corte(10);
                        
                    this.selecionarImagemCallbackContext.success(result);
                }catch (Exception e){
                    this.selecionarImagemCallbackContext.error("selecionarImagem error: " + e.toString());
                }
            }
        }else{
            Toast.makeText(mContext, "You haven't a picked Image", Toast.LENGTH_LONG).show();
        }
    }

}
