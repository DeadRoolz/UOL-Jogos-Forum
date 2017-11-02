/*
* Classe que armazena todos os emoticons do
* fórum e seus respectivos links e resource ids
*/

package com.gpsoft.uoljogosforum;

//java imports
import java.util.ArrayList;

public class EmoticonsList {
    
    private final int NUM_EMOTICONS = 136;
    private ArrayList<Emoticon> emoticons;
    
    
    public int size() {
        return emoticons.size();
    }
    
    public Emoticon get(int idx) {
        return emoticons.get(idx);
    }
    
    public EmoticonsList() {
        
        emoticons = new ArrayList<Emoticon>(NUM_EMOTICONS);
        
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_biggrin.gif", ":-D", R.drawable.icon_biggrin));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_smile.gif", ":-)", R.drawable.icon_smile));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_sad.gif", ":-(", R.drawable.icon_sad));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_surprised.gif", ":-o", R.drawable.icon_surprised));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_eek.gif", ":chocado:", R.drawable.icon_eek));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_confused.gif", ":-?", R.drawable.icon_confused));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_cool.gif", "8-)", R.drawable.icon_cool));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_lol.gif", ":lol:", R.drawable.icon_lol));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_mad.gif", ":-x", R.drawable.icon_mad));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_razz.gif", ":-P", R.drawable.icon_razz));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_redface.gif", ":oops:", R.drawable.icon_redface));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_cry.gif", ":chorar:", R.drawable.icon_cry));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_evil.gif", ":demonio:", R.drawable.icon_evil));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_twisted.gif", ":malefico:", R.drawable.icon_twisted));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_rolleyes.gif", ":roll:", R.drawable.icon_rolleyes));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_wink.gif", ";-)", R.drawable.icon_wink));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_exclaim.gif", ":!:", R.drawable.icon_exclaim));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_question.gif", ":?:", R.drawable.icon_question));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_idea.gif", ":ideia:", R.drawable.icon_idea));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_arrow.gif", ":seta:", R.drawable.icon_arrow));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_neutral.gif", ":-|", R.drawable.icon_neutral));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_mrgreen.gif", ":ironico:", R.drawable.icon_mrgreen));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_ha.gif", ":ha:", R.drawable.icon_ha));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_love.gif", ":love:", R.drawable.icon_love));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_awesome.gif", ":incrivel:", R.drawable.icon_awesome));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_coolcry.gif", ":chorar2:", R.drawable.icon_coolcry));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_cryingalot.gif", ":chorar3:", R.drawable.icon_cryingalot));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_cryingoffelicity.gif", ":chorar4:", R.drawable.icon_cryingoffelicity));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_crylolim.gif", ":lagrimas:", R.drawable.icon_crylolim));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_emosit.gif", ":girl:", R.drawable.icon_emosit));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_emotion.gif", ":emocao:", R.drawable.icon_emotion));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_facepalm.gif", ":cobrindorosto:", R.drawable.icon_facepalm));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_fuuu.gif", ":fuuu:", R.drawable.icon_fuuu));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_girlblink.gif", ":piscadela:", R.drawable.icon_girlblink));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_ilove.gif", ":gamado:", R.drawable.icon_ilove));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_lolsuper.gif", ":lolsuper:", R.drawable.icon_lolsuper));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_loveangry.gif", ":amorodio:", R.drawable.icon_loveangry));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_mustache.gif", ":bigode:", R.drawable.icon_mustache));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_not.gif", ":nem:", R.drawable.icon_not));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_nvm.gif", ":magoado:", R.drawable.icon_nvm));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_omg.gif", ":omg2:", R.drawable.icon_omg));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_omgrollt.gif", ":omg3:", R.drawable.icon_omgrollt));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_oopscool.gif", ":oopscool:", R.drawable.icon_oopscool));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_pinkarrow.gif", ":setarosa:", R.drawable.icon_pinkarrow));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_rimbuk.gif", ":rimbuk:", R.drawable.icon_rimbuk));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_rimkuk2.gif", ":rimbuk2:", R.drawable.icon_rimkuk2));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_rock.gif", ":rock:", R.drawable.icon_rock));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_shockblond.gif", ":girlshocked:", R.drawable.icon_shockblond));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_shocknorw.gif", ":girlhappy:", R.drawable.icon_shocknorw));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_shockpink.gif", ":girlsad:", R.drawable.icon_shockpink));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_sweattz.gif", ":suando:", R.drawable.icon_sweattz));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_vamp.gif", ":vamp:", R.drawable.icon_vamp));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_vryhappy.gif", ":feliz2:", R.drawable.icon_vryhappy));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_vrysad.gif", ":triste2:", R.drawable.icon_vrysad));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_wtff.gif", ":queixo:", R.drawable.icon_wtff));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_xis.gif", ":xis:", R.drawable.icon_xis));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_dc.gif", ":dc:", R.drawable.icon_dc));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_gba.gif", ":gba:", R.drawable.icon_gba));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_gbasp.gif", ":gbasp:", R.drawable.icon_gbasp));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_cube.gif", ":cube:", R.drawable.icon_cube));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_ps2.gif", ":ps2:", R.drawable.icon_ps2));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_xbox.gif", ":xbox:", R.drawable.icon_xbox));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_psx.gif", ":psx:", R.drawable.icon_psx));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_n64.gif", ":n64:", R.drawable.icon_n64));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_3do.gif", ":3do:", R.drawable.icon_3do));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_cube2.gif", ":cube2:", R.drawable.icon_cube2));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_dc2.gif", ":dc2:", R.drawable.icon_dc2));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_duo.gif", ":duo:", R.drawable.icon_duo));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_fc1.gif", ":famicom:", R.drawable.icon_fc1));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_fc2.gif", ":famicom2:", R.drawable.icon_fc2));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_gb.gif", ":gameboy:", R.drawable.icon_gb));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_gg.gif", ":gamegear:", R.drawable.icon_gg));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_jg.gif", ":jaguar:", R.drawable.icon_jg));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_lyx.gif", ":lynx:", R.drawable.icon_lyx));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_md.gif", ":megadrive:", R.drawable.icon_md));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_nc.gif", ":neogeocd:", R.drawable.icon_nc));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_nes.gif", ":nes:", R.drawable.icon_nes));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_np.gif", ":neogeopocket:", R.drawable.icon_np));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_pce.gif", ":pcengine:", R.drawable.icon_pce));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_ps1.gif", ":psone:", R.drawable.icon_ps1));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_ps22.gif", ":ps22:", R.drawable.icon_ps22));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_sat.gif", ":saturn:", R.drawable.icon_sat));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_sfc.gif", ":superfamicom:", R.drawable.icon_sfc));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_snes.gif", ":snes:", R.drawable.icon_snes));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_vb.gif", ":virtualboy:", R.drawable.icon_vb));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_ws.gif", ":wonderswan:", R.drawable.icon_ws));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_xbox2.gif", ":xb2:", R.drawable.icon_xbox2));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/32x.gif", ":32x:", R.drawable._32x));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_ngage.gif", ":ngage:", R.drawable.icon_ngage));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_ps31.gif", ":ps3:", R.drawable.icon_ps31));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_ps32.gif", ":ps3b:", R.drawable.icon_ps32));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_rev.gif", ":rev:", R.drawable.icon_rev));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_ds.gif", ":ds:", R.drawable.icon_ds));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_x360.gif", ":x360:", R.drawable.icon_x360));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_x360l.gif", ":x360b:", R.drawable.icon_x360l));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/icon_psp.gif", ":psp:", R.drawable.icon_psp));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/abc.gif", ":abc:", R.drawable.abc));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/america-rn.gif", ":america-rn:", R.drawable.america_rn));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/atletico-go.gif", ":atletico-go:", R.drawable.atletico_go));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/atletico-mg.gif", ":atletico-mg:", R.drawable.atletico_mg));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/atletico-pr.gif", ":atletico-pr:", R.drawable.atletico_pr));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/avai.gif", ":avai:", R.drawable.avai));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/bahia.gif", ":bahia:", R.drawable.bahia));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/botafogo.gif", ":botafogo:", R.drawable.botafogo));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/bragantino.gif", ":bragantino:", R.drawable.bragantino));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/brasiliense-df.gif", ":brasiliense-df:", R.drawable.brasiliense_df));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/campinense-pb.gif", ":campinense-pb:", R.drawable.campinense_pb));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/ceara.gif", ":ceara:", R.drawable.ceara));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/corinthians.gif", ":corinthians:", R.drawable.corinthians));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/coritiba.gif", ":coritiba:", R.drawable.coritiba));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/cruzeiro.gif", ":cruzeiro:", R.drawable.cruzeiro));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/duque-de-caxias.gif", ":duque-de-caxias:", R.drawable.duque_de_caxias));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/figueirense.gif", ":figueirense:", R.drawable.figueirense));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/flamengo.gif", ":flamengo:", R.drawable.flamengo));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/fluminense.gif", ":fluminense:", R.drawable.fluminense));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/fortaleza.gif", ":fortaleza:", R.drawable.fortaleza));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/goias.gif", ":goias:", R.drawable.goias));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/gremio.gif", ":gremio:", R.drawable.gremio));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/gremio-barueri.gif", ":gremio-barueri:", R.drawable.gremio_barueri));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/guarani.gif", ":guarani:", R.drawable.guarani));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/internacional.gif", ":internacional:", R.drawable.internacional));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/ipatinga-mg.gif", ":ipatinga-mg:", R.drawable.ipatinga_mg));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/juventude.gif", ":juventude:", R.drawable.juventude));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/nautico.gif", ":nautico:", R.drawable.nautico));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/palmeiras.gif", ":palmeiras:", R.drawable.palmeiras));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/parana-clube.gif", ":parana-clube:", R.drawable.parana_clube));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/ponte-preta.gif", ":ponte-preta:", R.drawable.ponte_preta));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/portuguesa.gif", ":portuguesa:", R.drawable.portuguesa));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/santo-andre.gif", ":santo-andre:", R.drawable.santo_andre));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/santos.gif", ":santos:", R.drawable.santos));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/sao-caetano.gif", ":sao-caetano:", R.drawable.sao_caetano));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/sao-paulo.gif", ":sao-paulo:", R.drawable.sao_paulo));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/sport.gif", ":sport:", R.drawable.sport));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/vasco.gif", ":vasco:", R.drawable.vasco));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/vila-nova-go.gif", ":vila-nova-go:", R.drawable.vila_nova_go));
        emoticons.add(new Emoticon("http://forum.imguol.com//forum/images/smiles/vitoria.gif", ":vitoria:", R.drawable.vitoria));
    }
    
}