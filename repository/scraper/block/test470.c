int test(int blkcg, int q, int update_hint){
if ( update_hint     )     { lockdep_assert_held ( q    -  >       queue_lock )    ;  rcu_assign_pointer ( blkcg    -  >          <missing ';'>   blkg_hint ,   blkg ) ;  }    }