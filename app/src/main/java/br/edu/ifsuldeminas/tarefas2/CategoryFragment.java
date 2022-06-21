package br.edu.ifsuldeminas.tarefas2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;

import br.edu.ifsuldeminas.tarefas2.databinding.FragmentCategoryBinding;
import br.edu.ifsuldeminas.tarefas2.db.CategorieDAO;
import br.edu.ifsuldeminas.tarefas2.domain.Category;

public class CategoryFragment extends Fragment {
    private Category categoria;
    private FragmentCategoryBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSavedCategoryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText descriptionTIET = binding.categoryDescription;

                String nome = descriptionTIET.getText().toString();
                nome = nome != null ? nome : "";

                if(nome.equals("")){
                    Toast.makeText(getContext(), R.string.category_description_empty,
                            Toast.LENGTH_SHORT).show();

                } else {
                    if(categoria == null) {
                        Category categoria = new Category(0, nome);

                        CategorieDAO dao = new CategorieDAO(getContext());
                        dao.save(categoria);

                        Toast.makeText(getContext(), R.string.task_saved,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        categoria.setNome(nome);

                        CategorieDAO dao = new CategorieDAO(getContext());
                        dao.update(categoria);

                        Toast.makeText(getContext(), R.string.task_updated,
                                Toast.LENGTH_SHORT).show();
                    }
                }

                NavController navController = Navigation.findNavController(
                        getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_TaskFragment_to_MainFragment);
                navController.popBackStack(R.id.TaskFragment, true);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume(){
        super.onResume();
        Object categoryArguments = null;
        if (getArguments() != null){
            categoryArguments = getArguments().getSerializable("category");
        }
        if (categoryArguments != null){
            categoria = (Category) categoryArguments;
            TextInputEditText categoryDescription = binding.categoryDescription;
            categoryDescription.setText(categoria.getNome());
        }

    }
}
