int test(int d, int struct, int p){
if ( d     )     {  struct  lvm_rec *  p = ( struct   lvm_rec *     )  d      ;   u16  lvm_version = be16_to_cpu ( p    -  >       version )          ;  char  tmp [ 64 ]  ;  if ( lvm_version  ==  1     )     { int  pp_size_log2 = be16_to_cpu ( p    -  >       pp_size )          ;  pp_bytes_size = 1   < <      pp_size_log2 ;  pp_blocks_size = pp_bytes_size   /  512        ;  snprintf ( tmp       , sizeof ( tmp       )         , " AIX LVM header version %u found\n"  , lvm_version       )    ;  vgda_len = be32_to_cpu ( p    -  >       vgda_len )          ;  vgda_sector = be32_to_cpu ( p    -  >                <missing ';'>   vgda_psn [ 0 ] ) ;  }    else { snprintf ( tmp       , sizeof ( tmp       )         , " unsupported AIX LVM version %d found\n"  , lvm_version       )    ;  }      strlcat ( state    -  >          <missing ';'>   pp_buf ,   tmp ,   PAGE_SIZE ) ;  put_dev_sector ( sect       )    ;  }    }