void test(int g, int tg){
if ( ist_empty ( g_stats_alloc_list       )       )     {  struct  throtl_grp *  tg = list_first_entry ( g_stats_alloc_list       , struct   throtl_grp     , stats_alloc_node       )          ;  swap ( tg    -  >          <missing ';'>   stats_cpu ,   stats_cpu ) ;  list_del_init ( g    -  >       stats_alloc_node )    ;  }    }